import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
}

group = "com.tooxx"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8


buildscript {
    repositories {
        maven {
            url = uri("http://nexus.na.xom.com:8081/repository/maven-central/")
        }
        maven {
            url = uri("http://nexus.na.xom.com:8081/repository/maven-flcit/")
        }
        maven {
            url = uri("http://nexus.na.xom.com:8081/repository/maven-hosted/")
        }
    }
}


repositories {
    maven {
        url = uri("http://nexus.na.xom.com:8081/repository/maven-central/")
    }
    maven {
        url = uri("http://nexus.na.xom.com:8081/repository/maven-flcit/")
    }
    maven {
        url = uri("http://nexus.na.xom.com:8081/repository/maven-hosted/")
    }
}



dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
