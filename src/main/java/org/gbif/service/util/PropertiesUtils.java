/*
 * Copyright 2015 Global Biodiversity Information Facility (GBIF)
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
package org.gbif.service.util;

import java.util.Properties;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

/**
 * Utilities related to {@link Properties}
 */
public class PropertiesUtils {

  /**
   * Filters and translates Properties with a prefix.
   * The resulting Properties will only include the properties that start with the provided prefix with that prefix
   * removed (e.g. myprefix.key1 will be returned as key1 if prefix = "myprefix.")
   *
   * @param properties to filter and translate
   * @param prefix prefix used to filter the properties. (e.g. "myprefix.")
   * @return new Properties object with filtered and translated properties. Never null.
   */
  public static Properties filterProperties(@NotNull final Properties properties, @NotNull String prefix) {
    Preconditions.checkNotNull(properties, "Can't filter a null Properties");
    Preconditions.checkState(StringUtils.isNotBlank(prefix), "Can't filter using a blank prefix", properties);

    Properties filtered = new Properties();
    for (String key : properties.stringPropertyNames()) {
      if (key.startsWith(prefix)) {
        filtered.setProperty(key.substring(prefix.length()), properties.getProperty(key));
      }
    }
    return filtered;
  }
}
