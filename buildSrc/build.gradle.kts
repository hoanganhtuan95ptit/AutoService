plugins {
    `kotlin-dsl` // dùng Kotlin DSL để viết plugin
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal() // nên thêm để Gradle tìm plugin DSL
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("com.android.tools.build:gradle:8.4.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.21")
}