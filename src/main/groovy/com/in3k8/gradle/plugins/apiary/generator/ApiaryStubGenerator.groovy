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

        compileClassPath.each {
            println ">>>>>>>>>>>>>>>>>> ${it}"
        }

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
        println "retrieved ${controllers.size()} beans"



        new FileWriter(new File(location)).withWriter { Writer out ->
            if (controllers.size() > 0) {
                producer.appendHeader(out)
            }
            controllers.each {
                List<Method> requestMappingMethods = findRequestMappedMethods(it)
                if (requestMappingMethods) {
                    producer.appendControllerHeader(out, it.value.getClass().getName())
                    requestMappingMethods.each {Method m ->
                        producer.appendMappingInfo(out, m)
                    }
                }

            }
            out.flush()
        }

    }

    private static Collection findRequestMappedMethods(Entry<String, Object> it) {
        return it.value.getClass().getMethods().findAll { Method m ->
            return m.getAnnotation(RequestMapping)
        }
    }
}
