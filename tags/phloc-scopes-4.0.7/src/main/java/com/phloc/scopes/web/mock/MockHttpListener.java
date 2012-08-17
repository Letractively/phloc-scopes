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
package com.phloc.scopes.web.mock;

import java.util.EventListener;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionListener;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.scopes.web.servlet.WebScopeListener;

/**
 * This class globally holds the HTTP listeners ({@link ServletContextListener}
 * , {@link HttpSessionListener} and {@link ServletRequestListener}) that are
 * triggered in tests.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MockHttpListener
{
  private static MockEventListenerList s_aDefaultListener = new MockEventListenerList ();
  private static MockEventListenerList s_aListener = new MockEventListenerList ();

  static
  {
    // Set default default listeners :)
    s_aDefaultListener.addListener (new WebScopeListener ());
    s_aDefaultListener.addListener (new MockServletRequestListener ());

    // Ensure that some listeners are present
    s_aListener.setFrom (s_aDefaultListener);
  }

  private MockHttpListener ()
  {}

  public static void addDefaultListener (@Nullable final EventListener aListener)
  {
    s_aDefaultListener.addListener (aListener);
  }

  public static void removeDefaultListeners (@Nonnull final Class <? extends EventListener> aListenerClass)
  {
    s_aDefaultListener.removeListeners (aListenerClass);
  }

  public static void removeAllDefaultListeners ()
  {
    s_aDefaultListener.removeAllListeners ();
  }

  public static void setToDefault ()
  {
    s_aListener.setFrom (s_aDefaultListener);
  }

  public static void addListener (@Nullable final EventListener aListener)
  {
    s_aListener.addListener (aListener);
  }

  public static void removeListeners (@Nonnull final Class <? extends EventListener> aListenerClass)
  {
    s_aListener.removeListeners (aListenerClass);
  }

  public static void removeAllListeners ()
  {
    s_aListener.removeAllListeners ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <ServletContextListener> getAllServletContextListeners ()
  {
    return s_aListener.getAllServletContextListeners ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <HttpSessionListener> getAllHttpSessionListeners ()
  {
    return s_aListener.getAllHttpSessionListeners ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <ServletRequestListener> getAllServletRequestListeners ()
  {
    return s_aListener.getAllServletRequestListeners ();
  }
}
