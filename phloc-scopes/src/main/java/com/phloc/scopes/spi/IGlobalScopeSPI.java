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
package com.phloc.scopes.spi;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.IsSPIInterface;
import com.phloc.scopes.nonweb.domain.IGlobalScope;

/**
 * SPI for handling the global scope lifecycle. Is invoked for non-web and web
 * scopes.
 * 
 * @author philip
 */
@IsSPIInterface
public interface IGlobalScopeSPI
{
  /**
   * Called after the global scope was started
   * 
   * @param aGlobalScope
   *        The global scope object to be used
   */
  void onGlobalScopeBegin (@Nonnull IGlobalScope aGlobalScope);

  /**
   * Called before the global scope is shut down
   * 
   * @param aGlobalScope
   *        The global scope object to be used
   */
  void onGlobalScopeEnd (@Nonnull IGlobalScope aGlobalScope);
}
