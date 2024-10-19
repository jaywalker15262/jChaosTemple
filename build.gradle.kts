plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://repo.powbot.org/releases")}
}

dependencies {
    implementation("org.powbot:client-sdk:[1.+,1.0.99-SNAPSHOT)")
    implementation("org.powbot:client-sdk-loader:[1.+,1.0.99-SNAPSHOT)")
    implementation("com.google.guava:guava:32.0.0-android") // for @Subscribe
}

kotlin {
    jvmToolchain(11)
}