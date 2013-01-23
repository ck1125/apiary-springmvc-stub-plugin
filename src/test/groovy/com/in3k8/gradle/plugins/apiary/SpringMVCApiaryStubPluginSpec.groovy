package com.in3k8.gradle.plugins.apiary

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

public class SpringMVCApiaryStubPluginSpec extends Specification {


    SpringMVCApiaryStubPlugin plugin = new SpringMVCApiaryStubPlugin()


    def 'adds task'() {
        given:
            Project project = ProjectBuilder.builder().build()

        when:
            plugin.apply project

        then:
            project.tasks.generateSpringMVCStub instanceof GenerateSpringMVCStubTask
    }

}
