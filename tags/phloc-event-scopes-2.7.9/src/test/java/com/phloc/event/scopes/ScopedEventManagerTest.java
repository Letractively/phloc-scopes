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
package com.phloc.event.scopes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.event.impl.BaseEvent;
import com.phloc.scopes.mgr.EScope;
import com.phloc.scopes.mock.ScopeTestRule;

/**
 * JUnit test for class {@link ScopedEventManager}.
 * 
 * @author philip
 */
public final class ScopedEventManagerTest
{
  @Rule
  public final TestRule m_aScopeRule = new ScopeTestRule ();

  @Test
  public void testScoping ()
  {
    // prepare processing
    final MockCountingObserver aGlobal = new MockCountingObserver ();
    final MockCountingObserver aApp = new MockCountingObserver ();
    final MockCountingObserver aRequest = new MockCountingObserver ();
    assertTrue (ScopedEventManager.registerObserver (EScope.GLOBAL, aGlobal).isChanged ());
    assertTrue (ScopedEventManager.registerObserver (EScope.APPLICATION, aApp).isChanged ());
    assertTrue (ScopedEventManager.registerObserver (EScope.REQUEST, aRequest).isChanged ());
    try
    {
      // check preconditions
      assertEquals (0, aGlobal.getInvocationCount ());
      assertEquals (0, aApp.getInvocationCount ());
      assertEquals (0, aRequest.getInvocationCount ());

      // process
      assertTrue (ScopedEventManager.notifyObservers (new BaseEvent (MockCountingObserver.TOPIC)));

      // check postconditions
      assertEquals (1, aGlobal.getInvocationCount ());
      assertEquals (1, aApp.getInvocationCount ());
      assertEquals (1, aRequest.getInvocationCount ());
    }
    finally
    {
      // unregister
      assertTrue (ScopedEventManager.unregisterObserver (EScope.GLOBAL, aGlobal).isChanged ());
      assertTrue (ScopedEventManager.unregisterObserver (EScope.APPLICATION, aApp).isChanged ());
      assertTrue (ScopedEventManager.unregisterObserver (EScope.REQUEST, aRequest).isChanged ());
    }
  }

  @Test
  public void testUnregister ()
  {
    final MockCountingObserver aGlobal = new MockCountingObserver ();
    assertTrue (ScopedEventManager.registerObserver (EScope.GLOBAL, aGlobal).isChanged ());
    assertTrue (ScopedEventManager.unregisterObserver (EScope.GLOBAL, aGlobal).isChanged ());

    // unregister again should fail :)
    assertTrue (ScopedEventManager.unregisterObserver (EScope.GLOBAL, aGlobal).isUnchanged ());
  }
}
