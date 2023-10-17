plugins {
    id("java")
    kotlin("jvm") version "1.8.0"
}

group = "revxrsal"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
}

kotlin {
    jvmToolchain(8)
}
