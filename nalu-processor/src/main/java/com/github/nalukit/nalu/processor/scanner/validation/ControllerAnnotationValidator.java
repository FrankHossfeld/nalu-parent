/*
 * Copyright (c) 2018 - 2019 - Frank Hossfeld
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
package com.github.nalukit.nalu.processor.scanner.validation;

import com.github.nalukit.nalu.client.component.IsController;
import com.github.nalukit.nalu.client.component.annotation.AcceptParameter;
import com.github.nalukit.nalu.client.component.annotation.Controller;
import com.github.nalukit.nalu.processor.ProcessorException;
import com.github.nalukit.nalu.processor.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllerAnnotationValidator {

  private ProcessorUtils processorUtils;

  private ProcessingEnvironment processingEnvironment;

  private RoundEnvironment roundEnvironment;

  private Element controllerElement;

  @SuppressWarnings("unused")
  private ControllerAnnotationValidator() {
  }

  private ControllerAnnotationValidator(Builder builder) {
    this.processingEnvironment = builder.processingEnvironment;
    this.roundEnvironment = builder.roundEnvironment;
    this.controllerElement = builder.controllerElement;
    setUp();
  }

  public static Builder builder() {
    return new Builder();
  }

  private void setUp() {
    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public void validate()
      throws ProcessorException {
    TypeElement typeElement = (TypeElement) this.controllerElement;
    // @Controller can only be used on a class
    if (!typeElement.getKind()
                    .isClass()) {
      throw new ProcessorException("Nalu-Processor: @Controller can only be used with an class");
    }
    // @Controller can only be used on a interface that extends IsController
    if (!(this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                      typeElement.asType(),
                                                      this.processingEnvironment.getElementUtils()
                                                                                .getTypeElement(IsController.class.getCanonicalName())
                                                                                .asType()))) {
      throw new ProcessorException("Nalu-Processor: @Controller can only be used on a class that extends IsController or IsShell");
    }
    // check if route start with "/"
    Controller controllerAnnotation = controllerElement.getAnnotation(Controller.class);
    if (!controllerAnnotation.route()
                             .startsWith("/")) {
      throw new ProcessorException("Nalu-Processor: @Controller - route attribute muss begin with a '/'");
    }
    // validate route
    validateRoute();
    // AcceptParameter annotation
    List<String> parametersFromRoute = this.getParametersFromRoute(controllerAnnotation.route());
    for (Element element : this.processingEnvironment.getElementUtils()
                                                     .getAllMembers((TypeElement) this.controllerElement)) {
      if (ElementKind.METHOD.equals(element.getKind())) {
        if (!Objects.isNull(element.getAnnotation(AcceptParameter.class))) {
          AcceptParameter annotation = element.getAnnotation(AcceptParameter.class);
          if (!parametersFromRoute.contains(annotation.value())) {
            throw new ProcessorException("Nalu-Processor: controller >>" + controllerElement.toString() + "<< - @AcceptParameter with value >>" + annotation.value() + "<< is not represented in the route as parameter");
          }
          ExecutableType executableType = (ExecutableType) element.asType();
          List<? extends TypeMirror> parameters = executableType.getParameterTypes();
          if (parameters.size() != 1) {
            throw new ProcessorException("Nalu-Processor: controller >>" + controllerElement.toString() + "<< - @AcceptParameter annotated on >>" + executableType.toString() + "<< need on parameter of type String");
          }
          if (!String.class.getCanonicalName()
                           .equals(parameters.get(0)
                                             .toString())) {
            throw new ProcessorException("Nalu-Processor: controller >>" + controllerElement.toString() + "<< - @AcceptParameter on >>" + element.toString() + "<< parameter has the wrong type -> must be a String");
          }
        }
      }
    }
  }

  private List<String> getParametersFromRoute(String route) {
    return Stream.of(route.split("/"))
                 .filter(s -> s.startsWith(":"))
                 .map(s -> s.substring(1))
                 .collect(Collectors.toList());
  }

  private void validateRoute()
      throws ProcessorException {
    Controller controllerAnnotation = this.controllerElement.getAnnotation(Controller.class);
    String route = controllerAnnotation.route();
    // extract route first:
    if (route.startsWith("/")) {
      route = route.substring(1);
    }
    // initial routes do not need a validation!
    if (route.length() == 0) {
      return;
    }
    boolean handlingParameter = false;
    String[] splits = route.split("/");
    for (String s : splits) {
      // handle "//" -> not allowed
      if (s.length() == 0) {
        throw new ProcessorException("Nalu-Processor: controller >>" +
                                     this.controllerElement.getEnclosingElement()
                                                           .toString() +
                                     "<<  - illegal route >>" +
                                     route +
                                     "<< -> '//' not allowed!");
      }
      // check if it is a parameter definition (starting with ':' at first position)
      if (s.startsWith(":")) {
        handlingParameter = true;
        // starts with a parameter ==> error
        if (route.length() == 0) {
          throw new ProcessorException("Nalu-Processor: controller >>" +
                                       this.controllerElement.getEnclosingElement()
                                                             .toString() +
                                       "<<  - illegal route >>" +
                                       route +
                                       "<< -> route cannot start with parameter");
        }
        if (s.length() == 1) {
          throw new ProcessorException("Nalu-Processor: controller >>" +
                                       this.controllerElement.getEnclosingElement()
                                                             .toString() +
                                       "<<  - illegal route >>" +
                                       route +
                                       "<< -> illegal parameter name!");
        }
        //      } else {
        //        if (handlingParameter) {
        //          throw new ProcessorException("Nalu-Processor: controller >>" +
        //                                       this.controllerElement.getEnclosingElement()
        //                                                             .toString() +
        //                                       "<<  - illegal route >>" +
        //                                       route +
        //                                       "<< -> illegal route!");
        //        }
      }
    }
  }

  private List<String> getParameterFromRoute(String route) {
    if (!route.contains("/:")) {
      return new ArrayList<>();
    }
    return Arrays.asList(route.substring(route.indexOf("/:") + 2)
                              .split("/:"));
  }

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;

    RoundEnvironment roundEnvironment;

    Element controllerElement;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public Builder roundEnvironment(RoundEnvironment roundEnvironment) {
      this.roundEnvironment = roundEnvironment;
      return this;
    }

    public Builder controllerElement(Element controllerElement) {
      this.controllerElement = controllerElement;
      return this;
    }

    public ControllerAnnotationValidator build() {
      return new ControllerAnnotationValidator(this);
    }

  }

}
