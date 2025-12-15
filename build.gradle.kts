plugins {
    kotlin("jvm") version "2.2.21"
}

dependencies {
    implementation("org.choco-solver:choco-solver:5.0.0-beta.1")
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