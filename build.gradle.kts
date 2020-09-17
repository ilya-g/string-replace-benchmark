plugins {
    kotlin("multiplatform") version "1.4.10"
    id("kotlinx.benchmark") version "0.2.0-dev-20"
}
group = "org.test.benchmarks"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://dl.bintray.com/kotlin/kotlinx")
    }
}
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("org.jetbrains.kotlinx:kotlinx.benchmark.runtime:0.2.0-dev-20")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api("org.jetbrains.kotlin:kotlin-stdlib")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

benchmark {
    configurations {
        named("main") {
            warmups = 7
            iterations = 10
            iterationTime = 1000
            iterationTimeUnit = "ms"
            mode = "AverageTime"
            outputTimeUnit = "ns"
        }

        register("ignoreCaseVsNeedle") {
            warmups = 7
            iterations = 20
            iterationTime = 500
            iterationTimeUnit = "ms"
            mode = "AverageTime"
            outputTimeUnit = "ns"
            param("totalLength", 5000)
            param("occurrences", 10)
        }

        register("lengthVsOccurrences") {
            warmups = 7
            iterations = 20
            iterationTime = 500
            iterationTimeUnit = "ms"
            mode = "AverageTime"
            outputTimeUnit = "ns"
            param("ignoreCase", false)
            param("needle", ">>back")
        }
        register("lengthVsOccurrences_ignoreCase") {
            warmups = 7
            iterations = 20
            iterationTime = 500
            iterationTimeUnit = "ms"
            mode = "AverageTime"
            outputTimeUnit = "ns"
            param("ignoreCase", true)
            param("needle", ">>back")
        }
    }

    targets {
        register("jvm") {
            this as kotlinx.benchmark.gradle.JvmBenchmarkTarget
            jmhVersion = "1.23"
        }
    }
}
