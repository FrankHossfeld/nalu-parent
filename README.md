# Nalu

[![License](https://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![GWT3/J2CL compatible](https://img.shields.io/badge/GWT3/J2CL-compatible-brightgreen.svg) 
[![Join the chat at https://gitter.im/Nalukit42/Lobby](https://badges.gitter.im/Nalukit42/Lobby.svg)](https://gitter.im/Nalukit42/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.nalukit/nalu.svg?colorB=44cc11)](https://search.maven.org/artifact/com.github.nalukit/nalu)
[![CI](https://github.com/NaluKit/nalu/workflows/CI/badge.svg)](https://github.com/NaluKit/nalu/actions)


Nalu is a tiny framework that helps you to create GWT based applications quite easily. Using the HTML 5 history for routing and navigation, Nalu supports the browser's back-, forward-, and reload-button by default and without any need to implement anything.

Nalu supports:

* Route based navigation

* HTML links and programmatically routing thanks to a router

* Browser's back- forward- and reload-button (In case the hasHistory-attribute of the `Application`-annotation is set to **true**)

* An optional loader that will be executed at application start to load data from the server

* Client side context, router and event bus which will be automatically injected in every controller, filter, loader and handler (Handlers have only access to the context and the event bus)

* Filters to intercept routing.

* Separation of views into a controller and a component with framework sided instantiation.

* A controller life-cycle using `start`-, `mayStop`- and `stop`- similar to GWT Activities.

* Popups

* Controller based handler manager, that will remove all handlers from the event bus in case the controller is stopped to prevent memory leaks (handler registrations must be added to the manager).

* UiBinder (nalu-plugin-gwt)

* Composites to support smaller units

* Controller & component caching

* Component creation inside a controller to support GWT replacement rules and static factory methods

* Multi Shell Support

* (nearly) Every widget lib (tested with: GWT, GXT, Domino-UI, Elemento, Elemental2, GWT-Material)

* Maven multi module projects to separate an application in smaller parts (module feature)

* Tracking user routing (in case a tracker is added)


## Basic Concept
Nalu uses a hash to navigate. **Starting with version 1.1.0 Nalu supports the use of hash less URLs.** Everything explained here will also work without hash. In case of working with a hash less URL, you need to implement something on the server to handle a reload.

Example hash:
```
#[shell]/[route]/:[parameter_1]/:[parameter_2]
```

where
* shell: the shell to be used to display the route
* route: navigation end point (a controller)
* parameter_x: parameters of the route

The following flow shows the steps, once a routing is initiated. The flow will end with appending the new component to the DOM.

![Route Flow](https://github.com/NaluKit/nalu/blob/master/etc/images/routeFlow.png)

To connect a component to a route, just create a controller class which extend
`AbstractComponentController` and add the controller annotation `@Controller`.
```JAVA
@Controller(route = "/shell/route",
            selector = "content",
            component = MyComponent.class,
            componentInterface = IMyComponent.class)
public class MyController
    extends AbstractComponentController<MyApplicationContext, IMyComponent, HTMLElement>
    implements ISearchComponent.Controller {
}
```
To navigate to a new route use inside a controller:
```JAVA
    this.router.route("/shell/route",
                      parameter_1,
                      parameter_2);
```
The router is injected in the controller. To route to a new component call the route method and add at least the new route. In case the route has parameters, just add them as additional parameters. (**Important note:** parameters are always Strings!)

## Using
To use Nalu add the following dependencies to your pom:
```XML
<dependency>
    <groupId>com.github.nalukit</groupId>
    <artifactId>nalu</artifactId>
    <version>R2D2-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.github.nalukit</groupId>
    <artifactId>nalu-processor</artifactId>
    <version>R2D2-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```
Depending on the widget set your project is using, add one of the following plugins:

If the project uses a widget set based on **Elemental2**, **Elemento** or **Domino-UI**, use the **Nalu-Plugin-Elemental2** by adding the following lines to your pom:
```XML
<dependency>
    <groupId>com.github.nalukit</groupId>
    <artifactId>nalu-plugin-elemental2</artifactId>
    <version>R2D2-SNAPSHOT</version>
</dependency>
```
For Elemento there's a dedicated plugin which supports `org.jboss.gwt.elemento.core.IsElement<E>` as widget type:
```XML
<dependency>
    <groupId>com.github.nalukit</groupId>
    <artifactId>nalu-plugin-elemento</artifactId>
    <version>R2D2-SNAPSHOT</version>
</dependency>
```
The **nalu-plugin-elemento** can also be used with Domino-ui.

**(These plugins are ready to use with J2CL / GWT 3)**

If your project uses a widget set based on **GWT** 2.8.2 or newer, use the **Nalu-Plugin-GWT** by adding the following lines to your pom:
```XML
    <dependency>
      <groupId>com.github.nalukit</groupId>
      <artifactId>nalu-plugin-gwt</artifactId>
      <version>R2D2-SNAPSHOT</version>
    </dependency>
    <dependency>
      <groupId>com.github.nalukit</groupId>
      <artifactId>nalu-plugin-gwt-processor</artifactId>
      <version>R2D2-SNAPSHOT</version>
      <scope>provided</scope>
    </dependency>
```
**(This plugin will not work with J2CL / GWT 3)**

See the wiki for more information about Nalu and how to use it.

## Wiki
More useful information about Nalu and how to use it, can be found inside the [Wiki](https://github.com/nalukit/nalu/wiki).

## J2CL / GWT3
With the next version of GWT (GWT 3) and the new J2CL transpiler, there will be major changes in the GWT development. For example: JSNI and generators, besides other things, will be gone. To be prepared for the future things like JSNI, generators or any other dependency to GWT has to be removed and must be avoided.

 Nalu uses only the already migrated `gwt-events` from `org.gwtproject`. (As long as gwt-events is not on Maven Central the sources of gwt-events will be part of Nalu. See **[#9 Replace gwt-event source files with gwt-event from Maven Central](https://github.com/NaluKit/nalu/issues/9)**)

Nalu has **no** dependency to gwt-user nor Nalu's dependencies! Nalu does not use JSNI, generators or anything else from GWT (except the nalu-plugin-gwt).

Nalu is ready to use with J2CL / GWT 3 as long as you do not use the nalu-plugin-gwt!

## To get in touch with the developer
Please use the [Nalu Gitter room](https://gitter.im/Nalukit42/Lobby).

## Examples
Here you will find many examples that show Nalu in action: [https://github.com/nalukit/nalu-examples](https://github.com/nalukit/nalu-examples).

## YouTube
You will find a session about Nalu at the GWT Community Channel [here](https://www.youtube.com/watch?v=GsHY4f3cvRU).

## Project Generator
To speed up creating a Nalu project, the [Nalu Boot Starter Project Generator](http://www.mvp4g.org/boot-starter-nalu/BootStarterNalu.html) (which is also based on Nalu) can be used to create a Nalu based project with a few clicks. The project generator will generate a Maven project, which can be imported to your preferred IDE. The project is ready to use.

Just run **mvn clean install** and **mvn gwt:devmode** to start the project.

More information about the generator and the source code can be found here: [Nalu Project Generator](https://github.com/nalukit/gwt-boot-starter-nalu).

## Notes
In case you find a bug, please open an issue or post it inside the [Nalu Gitter room](https://gitter.im/Nalukit42/Lobby).
