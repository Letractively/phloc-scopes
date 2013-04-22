/**
 * Copyright (C) 2006-2013 phloc systems
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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.scopes.factory.DefaultScopeFactory;
import com.phloc.scopes.factory.IScopeFactory;

/**
 * The meta scope factory holding both the factory for non-web scopes as well as
 * the factory for web-scopes.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class MetaScopeFactory
{
  private static IScopeFactory s_aScopeFactory = new DefaultScopeFactory ();

  private MetaScopeFactory ()
  {}

  /**
   * Set the default non-web scope factory
   * 
   * @param aScopeFactory
   *        The scope factory to use. May not be <code>null</code>.
   */
  public static void setScopeFactory (@Nonnull final IScopeFactory aScopeFactory)
  {
    if (aScopeFactory == null)
      throw new NullPointerException ("scopeFactory");
    s_aScopeFactory = aScopeFactory;
  }

  /**
   * @return The scope factory for non-web scopes. Never <code>null</code>.
   */
  @Nonnull
  public static IScopeFactory getScopeFactory ()
  {
    return s_aScopeFactory;
  }
}
