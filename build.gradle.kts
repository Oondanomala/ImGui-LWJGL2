plugins {
    java
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
    // For reference:
    //compileOnly(libs.imgui.java.lwjgl3)

    compileOnly(libs.imgui.java.binding)
    compileOnly(libs.imgui.java.natives.windows)
    compileOnly(libs.imgui.java.natives.linux)
    compileOnly(libs.imgui.java.natives.macos)

    // Optional dependency, but Minecraft already transitively depends on this through OSHI
    // Used for implementing mouse cursor shapes
    compileOnly("net.java.dev.jna:jna:3.4.0")
    compileOnly("net.java.dev.jna:platform:3.4.0")

    compileOnly("org.lwjgl.lwjgl:lwjgl:2.9.3")
}
