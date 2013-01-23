package com.in3k8.gradle.plugins.apiary

import org.gradle.api.Plugin
import org.gradle.api.Project


class SpringMVCApiaryStubPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {


        project.extensions.create('apiary-spring', SpringMVCApiaryStubPluginExtension)

        project.tasks.add('generateSpringMVCStub',GenerateSpringMVCStubTask)
    }

}

