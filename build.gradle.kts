plugins {
    kotlin("jvm") version "2.2.21"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}

kotlin {
    jvmToolchain(25)

}

java {
    // Kotlin 2.2 doesn't support Java 25 as target yet
    targetCompatibility = JavaVersion.VERSION_24
}