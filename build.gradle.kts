import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val kotlinVersion = "2.1.20"
val serializationVersion = "2.0.0"
val richeditorVersion = "1.0.0-rc12"
val commonsLangVersion = "3.17.0"
val leopardJavaVersion = "2.0.4"
val flatlafVersion = "3.5.4"
val annotationsVersion = "26.0.2"
val slf4jVersion = "2.0.17"
val mysqlConnectorVersion = "9.2.0"
val jAdapterForNativeTTSVersion = "0.12.1"
val coroutinesCoreVersion = "1.10.1"
val ktorVersion = "3.1.1"
val serializationJsonVersion = "1.8.0"
val datetimeJvmVersion = "0.6.2"
val kermitV = "2.0.5"

plugins {
  kotlin("jvm")
  id("org.jetbrains.compose")
  id("org.jetbrains.kotlin.plugin.compose")
  kotlin("plugin.serialization") version "2.1.20"
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
  implementation("com.mohamedrejeb.richeditor:richeditor-compose-desktop:$richeditorVersion")
  implementation("org.apache.commons:commons-lang3:$commonsLangVersion")
  implementation("ai.picovoice:leopard-java:$leopardJavaVersion")
  implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
  implementation("com.formdev:flatlaf:$flatlafVersion")
  implementation("org.jetbrains:annotations:$annotationsVersion")
  implementation("org.slf4j:slf4j-nop:$slf4jVersion")
  implementation("com.mysql:mysql-connector-j:$mysqlConnectorVersion")
  implementation("com.github.Darkrai9x:jAdapterForNativeTTS:$jAdapterForNativeTTSVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesCoreVersion")
  implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationJsonVersion")
  implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
  implementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:$datetimeJvmVersion")
  implementation("co.touchlab:kermit:$kermitV")
  implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktorVersion")
  implementation(compose.desktop.currentOs)
}

compose.desktop {
  application {
    mainClass = "aries.AppKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "Aries"
      packageVersion = "1.7.0"
    }
  }
}
