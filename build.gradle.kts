import com.bmuschko.gradle.docker.tasks.image.DockerSaveImage
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("org.jetbrains.kotlin.plugin.spring") version "2.1.10"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.gorylenko.gradle-git-properties") version "2.4.2"
    id("com.bmuschko.docker-remote-api") version "9.4.0"
}

group = "net.octosystems.smarthome"
version = "1.2.1"

private val dockerImageName = "ghcr.io/schmitzcatz/${project.name}:${project.version}"

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
    compileOnly("org.springframework.boot:spring-boot-devtools")
}

springBoot {
    buildInfo()
}

tasks.register<Copy>("dist") {
    dependsOn("bootJar")
    from(layout.buildDirectory.dir("libs"), "./LICENSE", "CHANGELOG.md")
    into(layout.buildDirectory.dir("dist"))
}
tasks.register<Zip>("distZip") {
    dependsOn("bootJar")
    archiveFileName = "${project.name}.zip"
    destinationDirectory = layout.buildDirectory.dir("dist")
    from(layout.buildDirectory.dir("libs"), "./LICENSE", "CHANGELOG.md")
}
tasks.register<DockerSaveImage>("distImage") {
    dependsOn("bootBuildImage")
    images.set(listOf(dockerImageName))
    useCompression.set(true)
    destFile.set(layout.buildDirectory.file("dist/${project.name}-image.tar.gz"))
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.withType<BootBuildImage> {
    imageName.set(dockerImageName)
    environment.set(
        mapOf(
            "BP_OCI_AUTHORS" to "Oliver Schmitz",
            "BP_OCI_CREATED" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            "BP_OCI_DESCRIPTION" to "Simple Web Application to display today's Tandoor Meal",
            "BP_OCI_DOCUMENTATION" to "https://github.com/schmitzCatz/tandoor-dashboard",
            "BP_OCI_LICENSES" to "GNU GPLv3",
            "BP_OCI_SOURCE" to "https://github.com/schmitzCatz/tandoor-dashboard",
            "BP_OCI_TITLE" to "Tandoor Dashboard",
            "BP_OCI_URL" to "https://github.com/schmitzCatz/tandoor-dashboard",
            "BP_OCI_VENDOR" to "Oliver Schmitz",
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
    publish.set(System.getenv("GITHUB_ACTOR")?.isNotEmpty()?: false)
    docker {
        publishRegistry {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

