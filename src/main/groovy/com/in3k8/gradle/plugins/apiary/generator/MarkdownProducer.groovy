package com.in3k8.gradle.plugins.apiary.generator

import java.lang.reflect.Method
import org.springframework.web.bind.annotation.RequestMapping


class MarkdownProducer {


    void appendControllerHeader(Writer out, String controllerName) {
        out.write("""
--
    Controller ${controllerName} Resources
--
""")
    }

    void appendMappingInfo(Writer out, Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping)
        out.write("""
Method ${method.name}
${mapping.method()[0]} ${mapping.value()[0]}
< 200
> ${mapping.produces() ? mapping.produces() : 'text/plain'}
""")

    }

    void appendHeader(Writer out) {
        out.write("""
HOST: http://www.yourdomain.com/

--- Sample API v2 ---
---
Welcome to the your sample API documentation. All comments can be written in (support [Markdown](http://daringfireball.net/projects/markdown/syntax) syntax)
---
""")
    }
}
