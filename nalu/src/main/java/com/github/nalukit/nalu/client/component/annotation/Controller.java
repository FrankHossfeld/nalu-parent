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

import com.github.nalukit.nalu.client.component.AbstractComponent;
import com.github.nalukit.nalu.client.component.IsAbstractComponent;
import com.github.nalukit.nalu.client.component.IsComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>This annotation is used to annotate a controller.
 * It defines the route and the selector.</p>
 * <br><br>
 * The annotation has the following attributes:
 * <ul>
 * <li>route:              name of the route which will display the controller in case of calling</li>
 * <li>selector:           id of the node where to insert the element</li>
 * <li>componentInterface: interface of the component</li>
 * <li>component:          class of the component</li>
 * </ul>
 *
 * @author Frank Hossfeld
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
  
  /**
   * <p>Declares the routes that the controller is bind to.</p>
   * <p>At least a controller needs one route.</p>
   *
   * @return the route/routes the controller is bind to
   */
  String[] route();
  
  /**
   * <p>Nalu uses Sots to present components. A Slot is addressed by a selector.
   * The selector defines the slot where the component of the controller is
   * presented.</p>
   * <p>The selector is the id of a node.</p>
   *
   * @return defines the slot to be used to present the component
   */
  String selector();
  
  Class<? extends IsComponent<?, ?>> componentInterface();
  
  Class<? extends IsAbstractComponent> component();
  
}
