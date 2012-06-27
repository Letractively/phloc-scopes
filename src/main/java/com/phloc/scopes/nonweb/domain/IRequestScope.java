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
package com.phloc.scopes.nonweb.domain;

import java.util.List;

import javax.annotation.Nullable;

import com.phloc.scopes.IScope;

/**
 * Interface for a single request scope object.
 * 
 * @author philip
 */
public interface IRequestScope extends IScope
{
  /**
   * @return The session ID associated with this request. May be
   *         <code>null</code>.
   */
  @Nullable
  String getSessionID ();

  /**
   * Get a list of all attribute values with the same name.
   * 
   * @param sName
   *        The name of the attribute to query.
   * @return <code>null</code> if no such attribute value exists
   */
  @Nullable
  List <String> getAttributeValues (@Nullable String sName);

  @Nullable
  List <String> getAttributeValues (@Nullable String sName, @Nullable List <String> aDefault);

  /**
   * Check if a attribute with the given name is present in the request and has
   * the specified value.
   * 
   * @param sName
   *        The name of the attribute to check
   * @param sDesiredValue
   *        The value to be matched
   * @return <code>true</code> if an attribute with the given name is present
   *         and has the desired value
   */
  boolean hasAttributeValue (@Nullable String sName, @Nullable String sDesiredValue);

  /**
   * Check if a attribute with the given name is present in the request and has
   * the specified value. If no such attribute is present, the passed default
   * value is returned.
   * 
   * @param sName
   *        The name of the attribute to check
   * @param sDesiredValue
   *        The value to be matched
   * @param bDefault
   *        the default value to be returned, if the specified attribute is not
   *        present
   * @return <code>true</code> if an attribute with the given name is present
   *         and has the desired value, <code>false</code> if the attribute is
   *         present but has a different value. If the attribute is not present,
   *         the default value is returned.
   */
  boolean hasAttributeValue (@Nullable String sName, @Nullable String sDesiredValue, boolean bDefault);
}
