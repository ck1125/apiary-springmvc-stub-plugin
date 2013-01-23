package com.in3k8.gradle.plugins.apiary

import com.in3k8.gradle.plugins.apiary.generator.ApiaryStubGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.compile.GroovyCompile

class GenerateSpringMVCStubTask extends DefaultTask {


    String getOutputLocation() {
        return new File(project.projectDir,'apiary.apib').absolutePath
    }

    @TaskAction
    void generateSpringMVCStub() {
        setDependsOn([':compileGroovy'])
        String configLocation = project.'apiary-spring'.configLocation

        List<URL> projectCompileClasspath =[project.sourceSets.getByName('main').output.classesDir.toURL(),
                                               project.sourceSets.getByName('main').output.resourcesDir.toURL()]

        project.getConfigurations().getByName('compile').resolvedConfiguration.resolvedArtifacts.each {

            println "++++++++++++++++ adding $it.file"
            projectCompileClasspath.add(it.file.toURL())
        }

        ApiaryStubGenerator generator = new ApiaryStubGenerator(configLocation,projectCompileClasspath)
        generator.generateSpringStub(outputLocation)


    }

    @Override
    String getDescription() {
        return 'Generates Apiary stub for Spring MVC project'
    }
}
