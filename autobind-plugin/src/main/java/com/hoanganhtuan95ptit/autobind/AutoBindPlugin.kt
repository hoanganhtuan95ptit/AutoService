package com.hoanganhtuan95ptit.autobind

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class AutoBindPlugin : Plugin<Project> {

    override fun apply(project: Project) {


        val kaptExtension = project.extensions.findByType(KaptExtension::class.java)

        kaptExtension?.let { kapt ->
            kapt.arguments {
                arg("moduleName", project.name)
            }
        }


        project.afterEvaluate {

            val android = project.extensions.findByName("android")

            val method = when (android?.javaClass?.canonicalName) {
                "com.android.build.gradle.internal.dsl.BaseAppModuleExtension_Decorated" -> {
                    android.javaClass.getMethod("getApplicationVariants")
                }

                "com.android.build.gradle.internal.dsl.DynamicFeatureExtension_Decorated" -> {
                    android.javaClass.getMethod("getApplicationVariants")
                }

                "com.android.build.gradle.LibraryExtension_Decorated" -> {
                    android.javaClass.getMethod("getLibraryVariants")
                }

                else -> {
                    return@afterEvaluate
                }
            }

            val variants = method.invoke(android) as Iterable<*>

            variants.forEach { variant ->

                val variantName = variant!!.javaClass.getMethod("getName").invoke(variant) as String

                processVariant(project, variant, variantName)
            }
        }
    }

    private fun processVariant(project: Project, variant: Any, variantName: String) {

        val sourceSets = variant.javaClass
            .getMethod("getSourceSets")
            .invoke(variant) as Iterable<*>

        sourceSets.forEach { ss ->

            processSourceSet(project, ss!!, variantName)
        }
    }

    private fun processSourceSet(project: Project, sourceSet: Any, variantName: String) {

        val assets = sourceSet.javaClass
            .getMethod("getAssets")
            .invoke(sourceSet)

        addAssetDir(assets, "${project.buildDir}/tmp/kapt3/classes/$variantName/assets/autobind")
    }

    private fun addAssetDir(assets: Any, path: String) {

        assets.javaClass
            .getMethod("srcDir", Any::class.java)
            .invoke(assets, path)
    }
}