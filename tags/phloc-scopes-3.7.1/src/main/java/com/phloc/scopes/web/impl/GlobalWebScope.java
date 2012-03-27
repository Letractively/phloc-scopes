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
package com.phloc.scopes.web.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.ServletContext;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.scopes.MetaScopeFactory;
import com.phloc.scopes.nonweb.domain.IGlobalScope;
import com.phloc.scopes.nonweb.impl.GlobalScope;
import com.phloc.scopes.web.domain.IApplicationWebScope;
import com.phloc.scopes.web.domain.IGlobalWebScope;

/**
 * Implementation of the {@link IGlobalScope} interface for web applications.<br>
 * Note: for synchronization issues, this class does not store the attributes in
 * the passed {@link ServletContext} but in a separate map.
 * 
 * @author philip
 */
@ThreadSafe
public final class GlobalWebScope extends GlobalScope implements IGlobalWebScope
{
  public static interface IContextPathProvider
  {
    @Nonnull
    String getContextPath ();
  }

  private final ServletContext m_aSC;
  private final IContextPathProvider m_aContextPathProvider;

  /**
   * Create a new {@link GlobalWebScope}. No objects are copied from the passed
   * {@link ServletContext} so this must be one of the very first action
   * 
   * @param aServletContext
   *        The servlet context to use. May not be <code>null</code>.
   * @param aContextPathProvider
   *        The context path provider. This is so weird, because the method
   *        <code>aServletContext.getContextPath ()</code> is only available in
   *        Servlet API &ge; 2.5. May not be <code>null</code>.
   */
  public GlobalWebScope (@Nonnull final ServletContext aServletContext,
                         @Nonnull final IContextPathProvider aContextPathProvider)
  {
    super (aServletContext.getServletContextName ());
    if (aContextPathProvider == null)
      throw new NullPointerException ("contextPathProvider");

    m_aSC = aServletContext;
    m_aContextPathProvider = aContextPathProvider;
  }

  @Override
  @Nonnull
  protected IApplicationWebScope createApplicationScope (@Nonnull @Nonempty final String sApplicationID)
  {
    return MetaScopeFactory.getWebScopeFactory ().createApplicationScope (sApplicationID);
  }

  @Override
  @Nullable
  public IApplicationWebScope getApplicationScope (@Nonnull @Nonempty final String sApplicationID,
                                                   final boolean bCreateIfNotExisting)
  {
    return (IApplicationWebScope) super.getApplicationScope (sApplicationID, bCreateIfNotExisting);
  }

  @Nonnull
  public ServletContext getServletContext ()
  {
    return m_aSC;
  }

  @Override
  @Nonnull
  public String getContextPath ()
  {
    // Must invoke the provider on demand, because with servlet-api < 2.5 there
    // is no method ServletContext.getContextPath and therefore it must be tken
    // from the request!!
    return m_aContextPathProvider.getContextPath ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final GlobalWebScope rhs = (GlobalWebScope) o;
    return getContextPath ().equals (rhs.getContextPath ());
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (getContextPath ()).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("servletContext", m_aSC)
                            .append ("contextPath", getContextPath ())
                            .toString ();
  }
}
