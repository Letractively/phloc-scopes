/**
 * Copyright (C) 2006-2012 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.scopes.web.mgr;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.scopes.MetaScopeFactory;
import com.phloc.scopes.spi.ScopeSPIManager;
import com.phloc.scopes.web.domain.ISessionWebScope;
import com.phloc.scopes.web.singleton.GlobalWebSingleton;

/**
 * Internal manager class for session web scopes.
 * 
 * @author philip
 */
public final class WebScopeSessionManager extends GlobalWebSingleton
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (WebScopeSessionManager.class);
  private static final IStatisticsHandlerCounter s_aUniqueSessionCounter = StatisticsManager.getCounterHandler (WebScopeSessionManager.class.getName () +
                                                                                                                "$UNIQUE_SESSIONS");

  /** All contained session scopes. */
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <String, ISessionWebScope> m_aSessionScopes = new HashMap <String, ISessionWebScope> ();
  private final Set <String> m_aSessionsInDestruction = new HashSet <String> ();

  @Deprecated
  @UsedViaReflection
  public WebScopeSessionManager ()
  {}

  @Nonnull
  public static WebScopeSessionManager getInstance ()
  {
    return getGlobalSingleton (WebScopeSessionManager.class);
  }

  /**
   * Get the session scope with the specified ID. If no such scope exists, no
   * further actions are taken.
   * 
   * @param sScopeID
   *        The ID to be resolved.
   * @return <code>null</code> if no such scope exists.
   */
  @Nullable
  public ISessionWebScope getSessionScopeOfID (@Nullable final String sScopeID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aSessionScopes.get (sScopeID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Get the session scope matching the passed HTTP session. If no such session
   * scope exists, it is created, even though this indicates, that your
   * HttpSessionListener is not working correctly!
   * 
   * @param aHttpSession
   *        The HTTP session for which the session scope is used.
   * @return The matching session web scope and never <code>null</code>.
   */
  @Nonnull
  public ISessionWebScope getSessionScope (@Nonnull final HttpSession aHttpSession)
  {
    if (aHttpSession == null)
      throw new NullPointerException ("httpSession");

    // Check if a matching session scope is present
    final String sSessionID = aHttpSession.getId ();
    ISessionWebScope aSessionScope = getSessionScopeOfID (sSessionID);
    if (aSessionScope == null)
    {
      // create new scope
      m_aRWLock.writeLock ().lock ();
      try
      {
        // Try in write-lock to be 100% sure
        aSessionScope = m_aSessionScopes.get (sSessionID);
        if (aSessionScope == null)
        {
          // This can e.g. happen in tests, when there are no registered
          // listeners for session events!
          s_aLogger.warn ("Creating a new session for ID '" +
                          sSessionID +
                          "' but there should already be one! Check your HttpSessionListener implementation.");
          aSessionScope = MetaScopeFactory.getWebScopeFactory ().createSessionScope (aHttpSession);
          m_aSessionScopes.put (sSessionID, aSessionScope);
          aSessionScope.initScope ();

          // Invoke SPIs
          ScopeSPIManager.onSessionScopeBegin (aSessionScope);
        }
      }
      finally
      {
        m_aRWLock.writeLock ().unlock ();
      }
    }
    return aSessionScope;
  }

  @Nonnull
  ISessionWebScope onSessionBegin (@Nonnull final HttpSession aHttpSession)
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("SESSION BEGIN: isNew? = " + aHttpSession.isNew ());
    final String sSessionID = aHttpSession.getId ();
    final ISessionWebScope aSessionScope = MetaScopeFactory.getWebScopeFactory ().createSessionScope (aHttpSession);

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aSessionScopes.put (sSessionID, aSessionScope) != null)
        s_aLogger.warn ("Overwriting session scope with ID '" + sSessionID + "'");
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    aSessionScope.initScope ();

    // Invoke SPIs
    ScopeSPIManager.onSessionScopeBegin (aSessionScope);

    s_aUniqueSessionCounter.increment ();
    return aSessionScope;
  }

  void onSessionEnd (@Nonnull final HttpSession aHttpSession)
  {
    // Remember session ID because after invalidation it is not accessible!
    // Note: for debugging only
    final String sSessionID = aHttpSession.getId ();

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Only if we're not just in destruction of exactly this session
      if (m_aSessionsInDestruction.add (sSessionID))
      {
        try
        {
          final ISessionWebScope aSessionScope = m_aSessionScopes.remove (sSessionID);
          if (aSessionScope != null)
          {
            // Invoke SPIs
            ScopeSPIManager.onSessionScopeEnd (aSessionScope);

            // Destroy the scope
            aSessionScope.destroyScope ();
          }
          else
          {
            // Ensure session is invalidated anyhow, even if no session scope is
            // present.
            // Happens in Tomcat startup if sessions that where serialized in
            // a previous invocation are invalidated on Tomcat restart
            s_aLogger.warn ("Found no session scope but invalidating session '" + sSessionID + "' anyway");
            try
            {
              aHttpSession.invalidate ();
            }
            catch (final IllegalStateException ex)
            {
              // session already invalidated
            }
          }
        }
        finally
        {
          m_aSessionsInDestruction.remove (sSessionID);
        }
      }
      else
        s_aLogger.info ("Already destructing session '" + sSessionID + "'");
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return The number of managed session scopes. Always &ge; 0.
   */
  @Nonnegative
  public int getSessionCount ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aSessionScopes.size ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return A non-<code>null</code>, mutable copy of all managed session
   *         scopes.
   */
  @Nonnull
  @ReturnsMutableCopy
  public Collection <? extends ISessionWebScope> getAllSessionScopes ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aSessionScopes.values ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  protected void onDestroy ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      // destroy all session scopes (make a copy, because we're invalidating the
      // sessions!)
      for (final ISessionWebScope aSessionScope : ContainerHelper.newList (m_aSessionScopes.values ()))
      {
        // Since the session is still open when we're shutting down the global
        // context, the session must also be invalidated!
        try
        {
          // Should implicitly trigger a call to onSessionEnd, which in case
          // triggers a call to aSessionScope.destroyScope
          aSessionScope.getSession ().invalidate ();
        }
        catch (final IllegalStateException ex)
        {
          s_aLogger.warn ("Session '" +
                          aSessionScope.getID () +
                          "' was already invalidated, but still in the session map!");

          // Destroy the scope manually so that all nested scopes are destroyed
          aSessionScope.destroyScope ();
        }
      }
      m_aSessionScopes.clear ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }
}