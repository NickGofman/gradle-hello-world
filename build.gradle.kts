// set variable
val versionToChange = "1.1.2"

// set version
version = versionToChange


plugins {
    kotlin("jvm") version "1.6.20"
    id("application")
    id("java")
    id("idea")

    // This is used to create a GraalVM native image
    id("org.graalvm.buildtools.native") version "0.9.11"

    // This creates a fat JAR
    id("com.github.johnrengelman.shadow") version "7.1.2"
}



group = "com.ido"
description = "HelloWorld"

application.mainClass.set("com.ido.HelloWorld")

repositories {
    mavenCentral()
}



graalvmNative {
    binaries {
        named("main") {
            imageName.set("helloworld")
            mainClass.set("com.ido.HelloWorld")
            fallback.set(false)
            sharedLibrary.set(false)
            useFatJar.set(true)
            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(17))
                vendor.set(JvmVendorSpec.matching("GraalVM Community"))
            })
        }
    }
}


tasks.create("incrementVersion") {
    group = "my tasks"
    description = "Increments the version in this build file everywhere it is used."
    fun generateVersion(): String {
        
        val updateMode = properties["mode"] ?: "minor" // By default, update the minor
        val (oldMajor, oldMinor, oldPatch) = versionToChange.split(".").map(String::toInt)
        var (newMajor, newMinor, newPatch) = arrayOf(oldMajor, oldMinor, 0)
        when (updateMode) {
            "major" -> newMajor = (oldMajor + 1).also { newMinor = 0 }
            "minor" -> newMinor = oldMinor + 1
            else -> newPatch = oldPatch + 1
        }
        return "$newMajor.$newMinor.$newPatch"
    }
    doLast {
        val newVersion = properties["overrideVersion"] as String? ?: generateVersion()
        val oldContent = buildFile.readText()
        val newContent = oldContent.replace("""= "$version"""", """= "$newVersion"""")
        buildFile.writeText(newContent)
    }
}
