# Apiary Stub Generator Gradle Plugin for Spring MVC projects

## Introduction
Gradle plugin that can be used on Spring MVC Web service projects for generating Apiary blueprint documents.
It is meant to non-intrusive and parses Spring projects that use JavaConfig, bootstraping the documentation of your APIs.

## Gradle Configuration
To use the plugin you will need the following in your build.gradle:

Tell Gradle to include the plugin in your build classpath

````groovy
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        classpath group:"com.in3k8.gradle.plugins", name: "apiary-springmvc-stub", version:"0.1-SNAPSHOT"
    }
}
````

Apply the plugin

````groovy
apply plugin: "apiary-springmvc-stub"
````

## Usage

````
gradle generateSpringMVCStub (gS for short)
````


