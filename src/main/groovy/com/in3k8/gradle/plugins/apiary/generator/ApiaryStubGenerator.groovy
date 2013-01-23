package com.in3k8.gradle.plugins.apiary.generator

import java.lang.reflect.Method
import java.util.HashMap.Entry
import org.springframework.mock.web.MockServletContext
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.context.support.XmlWebApplicationContext

class ApiaryStubGenerator {

    private MarkdownProducer producer = new MarkdownProducer()
    AbstractRefreshableWebApplicationContext context
    URLClassLoader classLoader

    ApiaryStubGenerator(String configLocation, List<URL> compileClassPath) {
        classLoader = new URLClassLoader(compileClassPath as URL[],this.getClass().classLoader)

        if (configLocation.contains('xml')) {
            context = new XmlWebApplicationContext(classLoader: classLoader)
        } else {
            context = new AnnotationConfigWebApplicationContext(classLoader: classLoader)
        }
        context.configLocation = configLocation
        context.setServletContext(new MockServletContext())

    }

    void generateSpringStub(String location) {

        context.refresh()
        context.start()

        Map<String, Object> controllers = context.getBeansWithAnnotation(Controller)

        produceApiBlueprint(controllers,location)

    }

    private void produceApiBlueprint(Map<String,Object> controllers,String location) {

        new FileWriter(new File(location)).withWriter { Writer out ->
            if (controllers.size() > 0) {
                producer.appendHeader(out)
                produceBluePrintContent(controllers, out)
                out.flush()
            }
        }
    }

    private void produceBluePrintContent(Map<String, Object> controllers, Writer out) {
        controllers.each { controller ->
            List<Method> requestMappingMethods = findRequestMappedMethods(controller.value)
            if (requestMappingMethods) {
                producer.appendControllerHeader(out, controller.value.getClass().getName())
                requestMappingMethods.each {Method controllerRequestMappedMethod ->
                    producer.appendMappingInfo(out, controllerRequestMappedMethod)
                }
            }

        }
    }

    private static Collection findRequestMappedMethods(Object controller) {
        return controller.getClass().getMethods().findAll { Method controllerMethod ->
            return controllerMethod.getAnnotation(RequestMapping)
        }
    }
}
