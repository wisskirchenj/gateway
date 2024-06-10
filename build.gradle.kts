import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.graalvm.buildtools.native") version "0.10.2"
}

graalvmNative {
    binaries.all {
        buildArgs.add("-H:-AddAllFileSystemProviders")
    }
}

val springCloudVersion = "2023.0.2"
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
    }
}

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(22))
}

group = "de.cofinpro"
version = "0.2.1-SNAPSHOT"
val dockerHubRepo = "wisskirchenj/"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.spring.io/snapshot") // to use snapshot versions
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-mvc")

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<BootBuildImage>("bootBuildImage") {
    // buildpacks.set(listOf("paketobuildpacks/java:latest")) // for JVM
    buildpacks.set(listOf("paketobuildpacks/java-native-image:latest"))
    builder.set("paketobuildpacks/builder-jammy-buildpackless-tiny")
    environment.put("BP_NATIVE_IMAGE_BUILD_ARGUMENTS", "-H:-AddAllFileSystemProviders")
    imageName.set(dockerHubRepo + rootProject.name + ":" + version)
    createdDate.set("now")
}