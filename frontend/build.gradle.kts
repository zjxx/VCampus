import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(
         "https://packages.jetbrains.team/maven/p/skija/maven"
    )

    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("io.netty:netty-all:4.1.68.Final")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.5.0")
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("org.apache.pdfbox:pdfbox:2.0.24")
    implementation("org.jetbrains.skija:skija-shared:0.93.6")
    implementation("org.bytedeco:javacv-platform:1.5.6")
    implementation("org.bytedeco:opencv-platform:4.5.3-1.5.6")



}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "demo1"
            packageVersion = "1.0.0"
        }
    }
}
