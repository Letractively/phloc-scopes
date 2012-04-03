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
package com.phloc.scopes.web.fileupload.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import com.phloc.scopes.web.fileupload.IRequestContext;

/**
 * <p>
 * Provides access to the request information needed for a request made to an
 * HTTP servlet.
 * </p>
 * 
 * @author <a href="mailto:martinc@apache.org">Martin Cooper</a>
 * @since FileUpload 1.1
 * @version $Id: ServletRequestContext.java 479262 2006-11-26 03:09:24Z niallp $
 */
public final class ServletRequestContext implements IRequestContext
{

  // ----------------------------------------------------- Instance Variables

  /**
   * The request for which the context is being provided.
   */
  private final HttpServletRequest _request;

  // ----------------------------------------------------------- Constructors

  /**
   * Construct a context for this request.
   * 
   * @param request
   *        The request to which this context applies.
   */
  public ServletRequestContext (final HttpServletRequest request)
  {
    this._request = request;
  }

  // --------------------------------------------------------- Public Methods

  /**
   * Retrieve the character encoding for the request.
   * 
   * @return The character encoding for the request.
   */
  public String getCharacterEncoding ()
  {
    return _request.getCharacterEncoding ();
  }

  /**
   * Retrieve the content type of the request.
   * 
   * @return The content type of the request.
   */
  public String getContentType ()
  {
    return _request.getContentType ();
  }

  /**
   * Retrieve the content length of the request.
   * 
   * @return The content length of the request.
   */
  public int getContentLength ()
  {
    return _request.getContentLength ();
  }

  /**
   * Retrieve the input stream for the request.
   * 
   * @return The input stream for the request.
   * @throws IOException
   *         if a problem occurs.
   */
  public InputStream getInputStream () throws IOException
  {
    return _request.getInputStream ();
  }

  /**
   * Returns a string representation of this object.
   * 
   * @return a string representation of this object.
   */
  @Override
  public String toString ()
  {
    return "ContentLength=" + this.getContentLength () + ", ContentType=" + this.getContentType ();
  }
}
