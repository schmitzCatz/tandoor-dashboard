import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.jetbrains.kotlin.plugin.spring") version "2.0.21"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.gorylenko.gradle-git-properties") version "2.4.2"
}

group = "net.octosystems.smarthome"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(kotlin("reflect"))

    runtimeOnly("org.webjars:bootstrap:5.3.3")
    runtimeOnly("org.webjars.npm:bootstrap-icons:1.11.3")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test-junit5"))

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

springBoot {
    buildInfo()
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}


tasks.withType<BootBuildImage> {
    imageName.set("ghcr.io/schmitzcatz/${project.name}:${project.version}")
    environment.set(
        mapOf(
            "BP_OCI_AUTHORS" to "Oliver Schmitz",
            "BP_OCI_CREATED" to "${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)}",
            "BP_OCI_DESCRIPTION" to "Simple Web Application to display today's Tandoor Meal",
            "BP_OCI_DOCUMENTATION" to "https://github.com/schmitzCatz/tandoor-dashboard",
            "BP_OCI_LICENSES" to "GNU GPLv3",
            "BP_OCI_SOURCE" to "https://github.com/schmitzCatz/tandoor-dashboard",
            "BP_OCI_TITLE" to "Tandoor Dashboard",
            "BP_OCI_URL" to "https://github.com/schmitzCatz/tandoor-dashboard",
            "BP_OCI_VENDOR" to "Oliver Schmitz",
            "BP_OCI_REF_NAME" to "",
            "BP_OCI_REVISION" to "",
            "BP_OCI_VERSION" to "${project.version}"
        )
    )
    buildpacks.set(
        listOf(
            "paketo-buildpacks/ca-certificates",
            "paketo-buildpacks/bellsoft-liberica",
            "paketo-buildpacks/syft",
            "paketo-buildpacks/executable-jar",
            "paketo-buildpacks/spring-boot",
            "gcr.io/paketo-buildpacks/image-labels"
        )
    )
    publish.set(true)
    docker {
        publishRegistry {
            token.set(System.getenv("GITHUB_TOKEN"))
        }
    }
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

