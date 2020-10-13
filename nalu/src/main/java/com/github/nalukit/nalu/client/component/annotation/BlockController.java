/*
 * Copyright (c) 2018 - 2020 - Frank Hossfeld
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package com.github.nalukit.nalu.client.component.annotation;

import com.github.nalukit.nalu.client.component.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>This annotation is used to annotate a block-controller</p>
 * <br><br>
 * The annotation has the following attributes:
 * <ul>
 * <li>name:               name of the block used to identifiy the conroller in case an event is fired.</li>
 * <li>componentInterface: interface of the component</li>
 * <li>component:          class of the component</li>
 * <li>condition:          condition class to control the visibility of the block (optional)</li>
 * </ul>
 *
 * @author Frank Hossfeld
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BlockController {
  
  String name();
  
  Class<? extends IsBlockComponent<?>> componentInterface();
  
  Class<? extends IsAbstractBlockComponent> component();
  
  /**
   * Conditional class to show the block depending on the return result
   * of the showBlock()-method.
   * <p>
   * This parameter is optional.
   * <p>
   * Default is AlwaysShowBlock.class, which will always show the block.
   *
   * @return the composite loader condition class
   */
  Class<? extends IsShowBlockCondition> condition() default AlwaysShowBlock.class;
  
}
