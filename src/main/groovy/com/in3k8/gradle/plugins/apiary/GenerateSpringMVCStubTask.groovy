package com.in3k8.gradle.plugins.apiary

import com.in3k8.gradle.plugins.apiary.generator.ApiaryStubGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ResolvedConfiguration
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction

class GenerateSpringMVCStubTask extends DefaultTask {

    private static final String APIARY_BLUEPRINT = 'apiary.apib'

    GenerateSpringMVCStubTask() {
        super()
        setDependsOn(['processResources','compileJava','compileGroovy'])
    }

    @OutputFile
    File getOutputLocation() {
        return new File(project.projectDir,APIARY_BLUEPRINT)
    }

    @TaskAction
    void generateSpringMVCStub() {

        String configLocation = project.'apiary-spring'.configLocation

        List<Object> projectCompileClasspath = prepareClasspath()

        ApiaryStubGenerator generator = new ApiaryStubGenerator(configLocation,projectCompileClasspath)
        generator.generateSpringStub(outputLocation.absolutePath)


    }

    private List<Object> prepareClasspath() {
        SourceSet mainSourceSet = project.sourceSets.getByName('main')

        List<URL> projectCompileClasspath = [mainSourceSet.output.classesDir.toURL(),
                                             mainSourceSet.output.resourcesDir.toURL()]

        ResolvedConfiguration resolvedConfiguration = project.getConfigurations().getByName('compile').resolvedConfiguration

        resolvedConfiguration.resolvedArtifacts.each {
                projectCompileClasspath.add(it.file.toURL())
        }

        return projectCompileClasspath
    }

    @Override
    String getDescription() {
        return 'Generates Apiary stub for Spring MVC project'
    }
}
