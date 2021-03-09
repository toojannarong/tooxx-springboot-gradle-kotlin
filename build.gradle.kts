import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.30"
	kotlin("plugin.spring") version "1.4.30"
	jacoco
// 	id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
}

group = "com.xom"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
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

tasks.test {
	finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
	dependsOn(tasks.test) // tests are required to run before generating the report
	reports {
		xml.isEnabled = false
		csv.isEnabled = true
		csv.destination = file("$buildDir/jacocoCsv/jacocoCsv.csv")
		html.destination = file("$buildDir/jacocoHtml")
	}
}


tasks.register<Copy>("copy") {
	from("$rootProject/pre-commit")
	into(".git/hooks")
}
tasks {
	build {
		dependsOn(":copy")
	}
}


fun String.runCommand(workingDir: File = file("./")): String {
	val parts = this.split("\\s".toRegex())
	val proc = ProcessBuilder(*parts.toTypedArray())
		.directory(workingDir)
		.redirectOutput(ProcessBuilder.Redirect.PIPE)
		.redirectError(ProcessBuilder.Redirect.PIPE)
		.start()

	proc.waitFor(1, TimeUnit.MINUTES)
	return proc.inputStream.bufferedReader().readText().trim()
}

val gitBranch = "git rev-parse --abbrev-ref HEAD".runCommand()
val gitTag = "git tag -l --points-at HEAD".runCommand()
val gitCommitId = "git rev-parse --short=8 HEAD".runCommand()
tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	manifest {
		attributes("gitBranch" to gitBranch)
		attributes("gitTag" to gitTag)
		attributes("gitCommitId" to gitCommitId)
	}
}



springBoot {
	//Spring Boot Actuator’s info endpoint automatically publishes information about build in the presence of a META-INF/build-info.properties
	buildInfo()
}
