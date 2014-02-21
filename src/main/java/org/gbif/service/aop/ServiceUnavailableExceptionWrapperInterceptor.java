/*
 * Copyright 2011 Global Biodiversity Information Facility (GBIF)
 *
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * This is a {@link MethodInterceptor} that intercepts <em>every</em> {@link Exception} being thrown in a method and
 * rethrows it wrapped in a {@link ServiceUnavailableException} except for the provided list of Exceptions which should
 * not be wrapped.
 * <p/>
 * The excepted Exceptions will be rethrown unchanged.
 */
public class ServiceUnavailableExceptionWrapperInterceptor implements MethodInterceptor {

  private final Set<Class<? extends Exception>> exceptedExceptions;

  /**
   * Every Exception will be wrapped.
   */
  public ServiceUnavailableExceptionWrapperInterceptor() {
    exceptedExceptions = Collections.emptySet();
  }

  /**
   * Wraps every Exception except the ones provided.
   *
   * @param exceptedExceptions a list of Exception classes which should not be wrapped.
   */
  public ServiceUnavailableExceptionWrapperInterceptor(Collection<Class<? extends Exception>> exceptedExceptions) {
    this.exceptedExceptions = new HashSet<Class<? extends Exception>>(exceptedExceptions);
  }

  /**
   * Wraps every Exception except the one provided.
   *
   * @param exceptedException this Exception will not be wrapped.
   */
  public ServiceUnavailableExceptionWrapperInterceptor(Class<? extends Exception> exceptedException) {
    exceptedExceptions = new HashSet<Class<? extends Exception>>(1);
    exceptedExceptions.add(exceptedException);
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    try {
      return invocation.proceed();
    } catch (Exception e) {
      if (exceptedExceptions.contains(e.getClass())) {
        throw e;
      } else {
        throw new ServiceUnavailableException("", e);
      }
    }
  }
}
