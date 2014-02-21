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
package org.gbif.service.guice;

import java.util.Map;
import java.util.Properties;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.PrivateModule;
import com.google.inject.name.Names;

/**
 * Abstract Guice Module to use for private service modules that depend on properties for configuration.
 * To use this instantiate this module using the constructors and provide the required properties.
 * All properties have to be prefixed with a custom, shared portion,
 * for example {@code checklistbank.db} as in {@code checklistbank.db.JDBC.driver}.
 * All properties will automatically be available as named strings in guice bindings.
 * To add custom bindings please use configureService instead of overriding configure().
 *
 * @see #configureService()
 */

public abstract class PrivateServiceModule extends PrivateModule {

  private final String propertyPrefix;
  private final Map<String, String> properties;

  // This is needed for some scenarios which need to install other modules relying on
  // the properties, but using different prefixes
  private final Properties verbatimProperties;

  /**
   * Uses the given properties to configure the service.
   *
   * @param properties to use
   */
  public PrivateServiceModule(String propertyPrefix, Properties properties) {
    this.propertyPrefix = propertyPrefix;
    this.verbatimProperties = properties;
    this.properties = buildProperties(properties);
  }

  /**
   * Filters out all properties that don't have a common prefix and removes that prefix for those properties that do
   * have it.
   *
   * @param properties to filter and translate
   *
   * @return immutable Map with the translated and filtered properties
   */
  private Map<String, String> buildProperties(Properties properties) {
    ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

    for (Map.Entry<String, String> entry : Maps.fromProperties(properties).entrySet()) {
      if (entry.getKey().startsWith(propertyPrefix)) {
        builder.put(entry.getKey().substring(propertyPrefix.length()), entry.getValue());
      }
    }

    return builder.build();
  }

  @Override
  protected final void configure() {
    Names.bindProperties(binder(), properties);
    configureService();
  }

  /**
   * Implement this method to configure the guice module.
   */
  protected abstract void configureService();

  /**
   * Only subclasses are intended to use this.
   *
   * @return The verbatim properties that the module was instantiated with (before trimming prefixes)
   */
  protected Properties getVerbatimProperties() {
    return verbatimProperties;
  }
}
