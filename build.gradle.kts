buildscript {
    repositories { mavenCentral() }
    dependencies { classpath("com.vanniktech:gradle-maven-publish-plugin:0.19.0") }
}
allprojects {
    repositories {
        mavenCentral()
    }
}