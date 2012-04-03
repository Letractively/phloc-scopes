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
package com.phloc.scopes.web.singleton;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.scopes.AbstractSingleton;
import com.phloc.scopes.web.domain.ISessionApplicationWebScope;
import com.phloc.scopes.web.mgr.WebScopeManager;

/**
 * This is the base class for singleton objects that reside in the
 * session-application scope.
 * 
 * @see com.phloc.scopes.web.mgr.EWebScope#SESSION_APPLICATION
 * @author philip
 */
public abstract class SessionApplicationSingleton extends AbstractSingleton implements Serializable
{
  protected SessionApplicationSingleton ()
  {
    super ("getSessionApplicationSingleton");
  }

  @Nonnull
  private static ISessionApplicationWebScope _getMyScope ()
  {
    return WebScopeManager.getSessionApplicationScope ();
  }

  @Override
  @Nonnull
  protected final ISessionApplicationWebScope getScope ()
  {
    return _getMyScope ();
  }

  @Nonnull
  protected static <T extends SessionApplicationSingleton> T getSessionApplicationSingleton (@Nonnull final Class <T> aClass)
  {
    return getSingleton (_getMyScope (), aClass);
  }

  @Nonnull
  public static final List <SessionApplicationSingleton> getAllSingletons ()
  {
    return getAllSingletons (_getMyScope (), SessionApplicationSingleton.class);
  }
}
