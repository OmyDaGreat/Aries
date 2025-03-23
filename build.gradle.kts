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
    implementation(libs.richeditor)
    implementation(libs.commonsLang)
    implementation(libs.leopardJava)
    implementation(libs.kotlin.stdlib)
    implementation(libs.flatlaf)
    implementation(libs.annotations)
    implementation(libs.slf4j.nop)
    implementation(libs.mysqlConnector)
    implementation(libs.jAdapterForNativeTTS)
    implementation(libs.coroutines.core)
    implementation(libs.ktor.client.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.client.cio)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kermit)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.coroutines.swing)
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
