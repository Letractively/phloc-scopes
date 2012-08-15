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

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.http.HttpSession;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.scopes.nonweb.mock.AbstractBeforeAfterTestRule;

@NotThreadSafe
public class WebScopeTestRule extends AbstractBeforeAfterTestRule
{
  public static final String MOCK_CONTEXT = "/MockContext";

  static
  {
    // Ensure that at least the default-default listeners are present
    MockHttpListener.setToDefault ();
  }

  private final Map <String, String> m_aServletContextInitParameters;
  private MockServletContext m_aServletContext;
  private MockHttpServletRequest m_aRequest;

  public WebScopeTestRule ()
  {
    this (null);
  }

  public WebScopeTestRule (@Nullable final Map <String, String> aServletContextInitParameters)
  {
    m_aServletContextInitParameters = aServletContextInitParameters;
  }

  @Nonnull
  @ReturnsMutableCopy
  @OverrideOnDemand
  public Map <String, String> getServletContextInitParameters ()
  {
    return ContainerHelper.newMap (m_aServletContextInitParameters);
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void before () throws Throwable
  {
    // Start global scope -> triggers events
    m_aServletContext = new MockServletContext (MOCK_CONTEXT, getServletContextInitParameters ());

    // Start request scope -> triggers events
    m_aRequest = new MockHttpServletRequest (m_aServletContext);
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void after () throws Throwable
  {
    if (m_aRequest != null)
    {
      // end request -> triggers events
      m_aRequest.invalidate ();
      m_aRequest = null;
    }

    if (m_aServletContext != null)
    {
      // shutdown global context -> triggers events
      m_aServletContext.invalidate ();
      m_aServletContext = null;
    }
  }

  @Nullable
  public final MockServletContext getServletContext ()
  {
    return m_aServletContext;
  }

  @Nullable
  public final MockHttpServletRequest getRequest ()
  {
    return m_aRequest;
  }

  @Nullable
  public final HttpSession getSession (final boolean bCreateIfNotExisting)
  {
    return m_aRequest == null ? null : m_aRequest.getSession (bCreateIfNotExisting);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("scInitParams", m_aServletContextInitParameters)
                                       .append ("sc", m_aServletContext)
                                       .append ("request", m_aRequest)
                                       .toString ();
  }
}
