//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
//    kotlin("jvm") version "1.6.10"
//    kotlin("kapt") version "1.6.10"

    kotlin("jvm") version "1.7.20"
    kotlin("kapt") version "1.7.20"
    id("maven-publish")
    idea
}
val id = "err"
group = "com.qxdzbc"
version = "1.0"

repositories {
    google()
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        name = "Compose for Desktop DEV"
    }
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
    withSourcesJar()
}

dependencies{
    implementation("com.michael-bull.kotlin-result:kotlin-result-jvm:1.1.12")
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = id
            version = version
            from(components["java"])
        }
    }
}
