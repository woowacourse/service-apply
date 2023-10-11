import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.6.21"
    id("org.springframework.boot") version "2.7.15"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("com.vaadin") version "0.8.0"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("org.flywaydb.flyway") version "7.15.0"
}

group = "io.github.woowacourse"
version = "3.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    maven {
        url = uri("http://maven.vaadin.com/vaadin-addons")
        isAllowInsecureProtocol = true
    }
}

extra["vaadinVersion"] = "14.8.20"
extra["kotlin-coroutines.version"] = "1.6.0"
extra["flyway.version"] = "7.15.0"

val asciidoctorExt: Configuration by configurations.creating
val snippetsDir by extra { "build/generated-snippets" }

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.apache.commons:commons-csv:1.5")
    implementation("org.apache.poi:poi-ooxml:4.1.2")
    implementation("dev.mett.vaadin:tooltip:1.7.0")
    implementation("com.amazonaws:aws-java-sdk-ses:1.11.880")
    implementation("com.amazonaws:aws-java-sdk-sqs:1.12.+")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.0")
    implementation("org.jetbrains:markdown:0.5.0")
    compileOnly("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito")
    }
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("com.ninja-squad:springmockk:3.1.2")
    testImplementation("io.mockk:mockk:1.13.7")
    testImplementation("io.kotest:kotest-runner-junit5:5.4.2")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
}

dependencyManagement {
    imports {
        mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
    ktlint {
        verbose.set(true)
        disabledRules.addAll("import-ordering")
    }
    flyway {
        url = "jdbc:mysql://localhost:53306/apply?characterEncoding=UTF-8&serverTimezone=UTC"
        user = "user"
        password = "password"
    }
    test {
        useJUnitPlatform()
        outputs.dir(snippetsDir)
    }
    asciidoctor {
        inputs.dir(snippetsDir)
        configurations("asciidoctorExt")
        dependsOn(test)
        baseDirFollowsSourceFile()
    }
    register<Copy>("copyDocs") {
        dependsOn(asciidoctor)
        from("${asciidoctor.get().outputDir}/index.html")
        into("src/main/resources/static/docs")
    }
    bootJar {
        dependsOn(asciidoctor)
        from("${asciidoctor.get().outputDir}/index.html") {
            into("static/docs")
        }
    }
}
