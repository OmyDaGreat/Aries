import org.jetbrains.compose.desktop.application.dsl.TargetFormat.AppImage
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi

plugins {
    idea
    kotlin("jvm")
    alias(libs.plugins.compose)
    alias(libs.plugins.hot.reload)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

group = "programatica.aries"
version = "2.1.1"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven("https://jitpack.io")
    mavenLocal()
}

kotlin {
    jvmToolchain(17)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.commons)
    implementation(libs.leopard)
    implementation(libs.tts)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.log)
    implementation(compose.materialIconsExtended)
    implementation(compose.components.resources)
    implementation(compose.desktop.currentOs)
    implementation(compose.material)
    implementation(compose.ui)
}

compose.desktop {
    application {
        mainClass = "aries.AppKt"

        nativeDistributions {
            targetFormats(AppImage, Msi, Dmg)
            packageName = "Aries"
            packageVersion = version.toString()

            includeAllModules = true

            windows {
                shortcut = true
            }
        }
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

tasks {
    withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    register("packageubuntu") {
        group = "actions"
        description = "Packages the application for Linux."
        dependsOn(named("packageAppImage"))
    }
    register("packagemacos") {
        group = "actions"
        description = "Packages the application for Mac."
        dependsOn(named("packageDmg"))
    }
    register("packagewindows") {
        group = "actions"
        description = "Packages the application for Windows."
        dependsOn(named("packageMsi"))
    }
}
