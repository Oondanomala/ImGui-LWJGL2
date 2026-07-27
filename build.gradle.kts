plugins {
    id("java")
}

group = "loutre.imgui"
version = "1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
repositories {
    mavenCentral {
        metadataSources {
            mavenPom()
            artifact()
            ignoreGradleMetadataRedirection()
        }
    }
}

dependencies {
    compileOnly(libs.imgui.java.binding)
    compileOnly(libs.imgui.java.lwjgl3)

    compileOnly(libs.imgui.java.natives.windows)
    compileOnly(libs.imgui.java.natives.linux)
    compileOnly(libs.imgui.java.natives.macos)

    compileOnly("org.lwjgl.lwjgl:lwjgl:2.9.3")
}
