import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

group = "programatica.aries"
version = "1.7.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven("https://jitpack.io")
}

dependencies {
    implementation(libs.annotations)
    implementation(libs.richeditor)
    implementation(libs.commons)
    implementation(libs.leopard)
    implementation(libs.flatlaf)
    implementation(libs.mysql)
    implementation(libs.tts)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.log)
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "aries.AppKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Aries"
            packageVersion = version.toString()
        }
    }
}
