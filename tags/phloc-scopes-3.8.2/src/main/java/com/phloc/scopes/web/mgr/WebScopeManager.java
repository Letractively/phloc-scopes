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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.phloc.scopes.MetaScopeFactory;
import com.phloc.scopes.nonweb.mgr.ScopeManager;
import com.phloc.scopes.web.domain.IApplicationWebScope;
import com.phloc.scopes.web.domain.IGlobalWebScope;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.domain.ISessionApplicationWebScope;
import com.phloc.scopes.web.domain.ISessionWebScope;

/**
 * This is the main manager class for web scope handling.
 * 
 * @author philip
 */
@Immutable
public final class WebScopeManager
{
  private WebScopeManager ()
  {}

  // --- global scope ---

  @Nonnull
  public static IGlobalWebScope onGlobalBegin (@Nonnull final ServletContext aServletContext)
  {
    final IGlobalWebScope aGlobalScope = MetaScopeFactory.getWebScopeFactory ().createGlobalScope (aServletContext);
    ScopeManager.setGlobalScope (aGlobalScope);
    return aGlobalScope;
  }

  public static boolean isGlobalScopePresent ()
  {
    return ScopeManager.isGlobalScopePresent ();
  }

  @Nonnull
  public static IGlobalWebScope getGlobalScope ()
  {
    // Note: if you get an exception here, and you're in the unit test, please
    // derived from AbstractWebScopeAwareTestCase
    return (IGlobalWebScope) ScopeManager.getGlobalScope ();
  }

  public static void onGlobalEnd ()
  {
    ScopeManager.onGlobalEnd ();
  }

  // --- application scope ---

  @Nonnull
  public static IApplicationWebScope getApplicationScope ()
  {
    return getApplicationScope (true);
  }

  @Nonnull
  public static IApplicationWebScope getApplicationScope (final boolean bCreateIfNotExisting)
  {
    return (IApplicationWebScope) ScopeManager.getApplicationScope (bCreateIfNotExisting);
  }

  // --- session scope ---

  @Nonnull
  public static ISessionWebScope onSessionBegin (@Nonnull final HttpSession aHttpSession)
  {
    return WebScopeSessionManager.getInstance ().onSessionBegin (aHttpSession);
  }

  @Nonnull
  public static ISessionWebScope getSessionScope ()
  {
    return getSessionScope (true);
  }

  @Nullable
  public static ISessionWebScope getSessionScope (final boolean bCreateIfNotExisting)
  {
    final HttpSession aHttpSession = getRequestScope ().getSession (bCreateIfNotExisting);
    return aHttpSession == null ? null : WebScopeSessionManager.getInstance ().getSessionScope (aHttpSession);
  }

  public static void onSessionEnd (@Nonnull final HttpSession aHttpSession)
  {
    WebScopeSessionManager.getInstance ().onSessionEnd (aHttpSession);
  }

  // --- session application scope ---

  @Nonnull
  public static ISessionApplicationWebScope getSessionApplicationScope ()
  {
    return getSessionApplicationScope (true);
  }

  @Nullable
  public static ISessionApplicationWebScope getSessionApplicationScope (final boolean bCreateIfNotExisting)
  {
    return getSessionApplicationScope (ScopeManager.getRequestApplicationID (), bCreateIfNotExisting);
  }

  @Nullable
  public static ISessionApplicationWebScope getSessionApplicationScope (@Nonnull final String sAppID,
                                                                        final boolean bCreateIfNotExisting)
  {
    final ISessionWebScope aSessionScope = getSessionScope (bCreateIfNotExisting);
    if (aSessionScope == null)
      return null;

    return aSessionScope.getSessionApplicationScope (sAppID, bCreateIfNotExisting);
  }

  // --- request scopes ---

  @Nonnull
  public static IRequestWebScope onRequestBegin (@Nonnull final String sApplicationID,
                                                 @Nonnull final HttpServletRequest aHttpRequest,
                                                 @Nonnull final HttpServletResponse aHttpResponse)
  {
    final IRequestWebScope aRequestScope = MetaScopeFactory.getWebScopeFactory ().createRequestScope (aHttpRequest,
                                                                                                      aHttpResponse);
    ScopeManager.setRequestScope (sApplicationID, aRequestScope);
    return aRequestScope;
  }

  public static boolean isRequestScopePresent ()
  {
    return ScopeManager.isRequestScopePresent ();
  }

  @Nonnull
  public static IRequestWebScope getRequestScope ()
  {
    // Just cast
    return (IRequestWebScope) ScopeManager.getRequestScope ();
  }

  public static void onRequestEnd ()
  {
    ScopeManager.onRequestEnd ();
  }
}
