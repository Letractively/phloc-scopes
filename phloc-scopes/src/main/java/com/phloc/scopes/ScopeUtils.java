/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.scopes;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;

/**
 * Global scope utility methods that don't nicely fit somewhere else.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class ScopeUtils
{
  public static final boolean DEFAULT_DEBUG_LIFE_CYCLE = false;
  public static final boolean DEFAULT_DEBUG_GLOBAL_SCOPE = false;
  public static final boolean DEFAULT_DEBUG_APPLICATION_SCOPE = false;
  public static final boolean DEFAULT_DEBUG_SESSION_SCOPE = false;
  public static final boolean DEFAULT_DEBUG_SESSION_APPLICATION_SCOPE = false;
  public static final boolean DEFAULT_DEBUG_REQUEST_SCOPE = false;
  public static final boolean DEFAULT_DEBUG_WITH_STACK_TRACE = false;

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static boolean s_bDebugLifeCycle = DEFAULT_DEBUG_LIFE_CYCLE;
  @GuardedBy ("s_aRWLock")
  private static boolean s_bDebugGlobalScope = DEFAULT_DEBUG_GLOBAL_SCOPE;
  @GuardedBy ("s_aRWLock")
  private static boolean s_bDebugApplicationScope = DEFAULT_DEBUG_APPLICATION_SCOPE;
  @GuardedBy ("s_aRWLock")
  private static boolean s_bDebugSessionScope = DEFAULT_DEBUG_SESSION_SCOPE;
  @GuardedBy ("s_aRWLock")
  private static boolean s_bDebugSessionApplicationScope = DEFAULT_DEBUG_SESSION_APPLICATION_SCOPE;
  @GuardedBy ("s_aRWLock")
  private static boolean s_bDebugRequestScope = DEFAULT_DEBUG_REQUEST_SCOPE;
  @GuardedBy ("s_aRWLock")
  private static boolean s_bDebugWithStackTrace = DEFAULT_DEBUG_WITH_STACK_TRACE;

  private ScopeUtils ()
  {}

  /**
   * Enable or disable scope life cycle debugging for all scopes.
   * 
   * @param bDebugLifeCycle
   *        <code>true</code> if the scope life cycle should be debugged,
   *        <code>false</code> to disable it. By default is is disabled.
   */
  public static void setLifeCycleDebuggingEnabled (final boolean bDebugLifeCycle)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_bDebugLifeCycle = bDebugLifeCycle;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> if life cycle debugging for all scopes is
   *         enabled, <code>false</code> if it is disabled. The default value is
   *         disabled.
   */
  public static boolean isLifeCycleDebuggingEnabled ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bDebugLifeCycle;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Enable or disable global scope life cycle debugging.
   * 
   * @param bDebugScope
   *        <code>true</code> if the global scope life cycle should be debugged,
   *        <code>false</code> to disable it. By default is is disabled.
   */
  public static void setDebugGlobalScopeEnabled (final boolean bDebugScope)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_bDebugGlobalScope = bDebugScope;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> if global scope life cycle debugging is enabled,
   *         <code>false</code> if it is disabled. The default value is
   *         disabled.
   */
  public static boolean isDebugGlobalScopeEnabled ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bDebugGlobalScope;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Enable or disable application scope life cycle debugging.
   * 
   * @param bDebugScope
   *        <code>true</code> if the application scope life cycle should be
   *        debugged, <code>false</code> to disable it. By default is is
   *        disabled.
   */
  public static void setDebugApplicationScopeEnabled (final boolean bDebugScope)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_bDebugApplicationScope = bDebugScope;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> if application scope life cycle debugging is
   *         enabled, <code>false</code> if it is disabled. The default value is
   *         disabled.
   */
  public static boolean isDebugApplicationScopeEnabled ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bDebugApplicationScope;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Enable or disable session scope life cycle debugging.
   * 
   * @param bDebugScope
   *        <code>true</code> if the session scope life cycle should be
   *        debugged, <code>false</code> to disable it. By default is is
   *        disabled.
   */
  public static void setDebugSessionScopeEnabled (final boolean bDebugScope)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_bDebugSessionScope = bDebugScope;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> if session scope life cycle debugging is enabled,
   *         <code>false</code> if it is disabled. The default value is
   *         disabled.
   */
  public static boolean isDebugSessionScopeEnabled ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bDebugSessionScope;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Enable or disable session application scope life cycle debugging.
   * 
   * @param bDebugScope
   *        <code>true</code> if the session application scope life cycle should
   *        be debugged, <code>false</code> to disable it. By default is is
   *        disabled.
   */
  public static void setDebugSessionApplicationScopeEnabled (final boolean bDebugScope)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_bDebugSessionApplicationScope = bDebugScope;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> if session application scope life cycle debugging
   *         is enabled, <code>false</code> if it is disabled. The default value
   *         is disabled.
   */
  public static boolean isDebugSessionApplicationScopeEnabled ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bDebugSessionApplicationScope;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Enable or disable request scope life cycle debugging.
   * 
   * @param bDebugScope
   *        <code>true</code> if the request scope life cycle should be
   *        debugged, <code>false</code> to disable it. By default is is
   *        disabled.
   */
  public static void setDebugRequestScopeEnabled (final boolean bDebugScope)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_bDebugRequestScope = bDebugScope;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> if request scope life cycle debugging is enabled,
   *         <code>false</code> if it is disabled. The default value is
   *         disabled.
   */
  public static boolean isDebugRequestScopeEnabled ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bDebugRequestScope;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Enable or disable stack traces when debugging scopes.
   * 
   * @param bDebugWithStackTrace
   *        <code>true</code> to enable stack traces, <code>false</code> to
   *        disable them. By default is is disabled.
   */
  public static void setDebugWithStackTrace (final boolean bDebugWithStackTrace)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_bDebugWithStackTrace = bDebugWithStackTrace;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> if stack traces should be logged,
   *         <code>false</code> if not. The default value is disabled.
   */
  public static boolean isDebugWithStackTrace ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bDebugWithStackTrace;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * This is a just a helper method to determine whether scope creation/deletion
   * issues should be logged or not.
   * 
   * @param aLogger
   *        The logger to check.
   * @return <code>true</code> if scope creation/deletion should be logged,
   *         <code>false</code> otherwise.
   */
  @Deprecated
  public static boolean debugScopeLifeCycle (@Nonnull final Logger aLogger)
  {
    return isLifeCycleDebuggingEnabled () && aLogger.isInfoEnabled ();
  }

  /**
   * This is a just a helper method to determine whether global scope
   * creation/deletion issues should be logged or not.
   * 
   * @param aLogger
   *        The logger to check.
   * @return <code>true</code> if global scope creation/deletion should be
   *         logged, <code>false</code> otherwise.
   */
  public static boolean debugGlobalScopeLifeCycle (@Nonnull final Logger aLogger)
  {
    return (isLifeCycleDebuggingEnabled () || isDebugGlobalScopeEnabled ()) && aLogger.isInfoEnabled ();
  }

  /**
   * This is a just a helper method to determine whether application scope
   * creation/deletion issues should be logged or not.
   * 
   * @param aLogger
   *        The logger to check.
   * @return <code>true</code> if application scope creation/deletion should be
   *         logged, <code>false</code> otherwise.
   */
  public static boolean debugApplicationScopeLifeCycle (@Nonnull final Logger aLogger)
  {
    return (isLifeCycleDebuggingEnabled () || isDebugApplicationScopeEnabled ()) && aLogger.isInfoEnabled ();
  }

  /**
   * This is a just a helper method to determine whether session scope
   * creation/deletion issues should be logged or not.
   * 
   * @param aLogger
   *        The logger to check.
   * @return <code>true</code> if session scope creation/deletion should be
   *         logged, <code>false</code> otherwise.
   */
  public static boolean debugSessionScopeLifeCycle (@Nonnull final Logger aLogger)
  {
    return (isLifeCycleDebuggingEnabled () || isDebugSessionScopeEnabled ()) && aLogger.isInfoEnabled ();
  }

  /**
   * This is a just a helper method to determine whether global session
   * application creation/deletion issues should be logged or not.
   * 
   * @param aLogger
   *        The logger to check.
   * @return <code>true</code> if session application scope creation/deletion
   *         should be logged, <code>false</code> otherwise.
   */
  public static boolean debugSessionApplicationScopeLifeCycle (@Nonnull final Logger aLogger)
  {
    return (isLifeCycleDebuggingEnabled () || isDebugSessionApplicationScopeEnabled ()) && aLogger.isInfoEnabled ();
  }

  /**
   * This is a just a helper method to determine whether request scope
   * creation/deletion issues should be logged or not.
   * 
   * @param aLogger
   *        The logger to check.
   * @return <code>true</code> if request scope creation/deletion should be
   *         logged, <code>false</code> otherwise.
   */
  public static boolean debugRequestScopeLifeCycle (@Nonnull final Logger aLogger)
  {
    return (isLifeCycleDebuggingEnabled () || isDebugRequestScopeEnabled ()) && aLogger.isInfoEnabled ();
  }

  /**
   * @return An exception with the current stack trace or <code>null</code> if
   *         {@link #isDebugWithStackTrace()} is <code>false</code>
   * @see #isDebugWithStackTrace()
   */
  @Nullable
  public static Throwable getDebugStackTrace ()
  {
    return isDebugWithStackTrace () ? new Exception () : null;
  }
}
