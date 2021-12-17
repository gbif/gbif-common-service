/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gbif.service.aop;

import org.gbif.api.exception.ServiceUnavailableException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceUnavailableExceptionWrapperInterceptorTest {

  private MethodInterceptor interceptor;

  @Mock
  private MethodInvocation invocation;

  @Before
  public void setUp() {
    interceptor = new ServiceUnavailableExceptionWrapperInterceptor();
  }

  @Test(expected = ServiceUnavailableException.class)
  public void testException() throws Throwable {
    when(invocation.proceed()).thenThrow(new Exception("Test Exception"));
    interceptor.invoke(invocation);
  }

  @Test
  public void testNoException() throws Throwable {
    Object obj = new Object();
    when(invocation.proceed()).thenReturn(obj);
    Object result = interceptor.invoke(invocation);

    assertEquals(obj, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptedExceptions() throws Throwable {
    interceptor = new ServiceUnavailableExceptionWrapperInterceptor(IllegalArgumentException.class);

    when(invocation.proceed()).thenThrow(new IllegalArgumentException("Foo"));
    interceptor.invoke(invocation);
  }

  @Test(expected = ServiceUnavailableException.class)
  public void testExceptedExceptions2() throws Throwable {
    interceptor = new ServiceUnavailableExceptionWrapperInterceptor(IllegalArgumentException.class);

    when(invocation.proceed()).thenThrow(new Exception("Foo"));
    interceptor.invoke(invocation);
  }

}
