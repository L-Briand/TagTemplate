plugins {
    kotlin("jvm") version "1.6.20"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

group = rootProject.property("GROUP") as String
version = rootProject.property("VERSION_NAME") as String

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":lib"))
}

// KtLint configuration
configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    disabledRules.set(setOf("no-wildcard-imports"))
}
