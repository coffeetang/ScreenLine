import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ScreenLine"
            packageVersion = "1.0.0"
            macOS {
                packageVersion = "1.0.0"
                dmgPackageVersion = "1.0.0"
                dockName = "线条屏保"
                packageBuildVersion = "1.0.0"
                dmgPackageBuildVersion = "1.0.0"
                // 设置图标
                iconFile.set(project.file("line.icns"))
            }
        }
    }
}
