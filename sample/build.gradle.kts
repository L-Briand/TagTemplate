plugins {
    kotlin("jvm") version "1.6.20"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    application
}

group = rootProject.property("group") as String
version = rootProject.property("libVersion") as String
val main = "${project.group}.sample.Main"

application {
    mainClass.set(main)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<JavaCompile>().all {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation(project(":lib"))
}

// KtLint configuration
configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    disabledRules.set(setOf("no-wildcard-imports"))
}