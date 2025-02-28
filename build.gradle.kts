import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val kotlinVersion = "2.0.0"
val serializationVersion = "2.0.0"
val richeditorVersion = "1.0.0-rc06"
val commonsLangVersion = "3.14.0"
val leopardJavaVersion = "2.0.2"
val flatlafVersion = "3.4.1"
val annotationsVersion = "13.0"
val slf4jVersion = "2.0.13"
val mysqlConnectorVersion = "9.2.0"
val jAdapterForNativeTTSVersion = "0.12.1"
val coroutinesCoreVersion = "1.9.0-RC"
val ktorVersion = "2.3.12"
val serializationJsonVersion = "1.7.1"
val datetimeJvmVersion = "0.6.1"
val kermitV = "2.0.4"

plugins {
  kotlin("jvm")
  id("org.jetbrains.compose")
  id("org.jetbrains.kotlin.plugin.compose")
  kotlin("plugin.serialization") version "2.0.0"
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
