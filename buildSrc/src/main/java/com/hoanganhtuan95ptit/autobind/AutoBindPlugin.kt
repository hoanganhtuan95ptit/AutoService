package com.hoanganhtuan95ptit.autobind

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.DynamicFeatureExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class AutoBindPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val kaptExtension = project.extensions.findByType(KaptExtension::class.java)

        kaptExtension?.let { kapt ->
            // Thêm argument moduleName
            kapt.arguments {
                arg("moduleName", project.name)
            }
        }


        val android = project.extensions.findByName("android") as? com.android.build.gradle.BaseExtension
        if (android == null) {
            project.logger.lifecycle("AutoBind: no android extension in ${project.name}")
            return
        }

        project.afterEvaluate {

            val variants = when {
                android is AppExtension -> android.applicationVariants
                android is DynamicFeatureExtension -> android.applicationVariants
                android is LibraryExtension -> android.libraryVariants
                else -> emptyList()
            }

            variants.forEach { variant ->

                val variantName = variant.name

                val assetDir = project.file("${project.buildDir}/tmp/kapt3/classes/$variantName/assets/autobind")

                val sourceSet = android.sourceSets.getByName(variantName)
                sourceSet.assets.srcDir(assetDir)
            }
        }
    }
}
