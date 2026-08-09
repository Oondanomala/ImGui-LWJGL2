# Java ImGui LWJGL2 Backend

A backend for [ImGui-Java](https://github.com/SpaiR/imgui-java) `1.92.7.1` that uses [LWJGL2](https://github.com/lwjgl/LWJGL).

## Supported Extra Features

- Mouse cursor shapes

Please help to add more features by contributing!

## Using In Your Projects

This project follows SemVer, so breaking changes will only happen when the major version is incremented.

`build.gradle.kts`:
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementations("com.github.Oondanomala:ImGui-LWJGL2:1.0.1")
}
```
