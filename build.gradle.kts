import com.google.protobuf.gradle.*
plugins {
    id("io.micronaut.application") version "4.5.4"
    id("com.google.protobuf") version "0.9.4"
    id("com.gradleup.shadow") version "8.3.7"
    id("io.micronaut.aot") version "4.5.4"
}

version = "0.1"
group = "org.charitable.app"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Annotation processors - use compatible versions
    annotationProcessor("io.micronaut:micronaut-http-validation:4.9.4")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor:2.12.0")

    // PostgreSQL JDBC Driver
    runtimeOnly("org.postgresql:postgresql:42.7.3")

    // Micronaut Data JDBC
    implementation("io.micronaut.sql:micronaut-jdbc-hikari:5.7.0")


    // Core Micronaut dependencies - use 4.9.4 for consistency
    implementation("io.micronaut:micronaut-discovery-core:4.9.4")
    implementation("io.micronaut.grpc:micronaut-grpc-runtime:4.11.0")
    implementation("io.micronaut.validation:micronaut-validation:4.9.0")

    // for server reflection
    implementation("io.grpc:grpc-services")

    //security (for bcrypt)
    implementation("org.springframework.security:spring-security-crypto:6.1.5")
    // for logging with spring implementation
    implementation("org.springframework:spring-jcl:5.3.25")

    // pretty print json
    implementation("com.google.code.gson:gson:2.10.1")

    // for cli (like seeding db)
    implementation("io.micronaut.picocli:micronaut-picocli")

    // Validation
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    // Security
    implementation("io.micronaut.security:micronaut-security:4.11.5")

    // jwt security
    implementation("io.micronaut.security:micronaut-security-jwt")

    // json web token
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

    // Serialization
    implementation("io.micronaut.serde:micronaut-serde-jackson:2.12.0")

    // Annotations
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // JPA support
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa:4.9.0")

    // Lombok
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    compileOnly("org.projectlombok:lombok:1.18.30")

    // HTTP client and logging
    compileOnly("io.micronaut:micronaut-http-client:4.9.4")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.14")
    testImplementation("io.micronaut:micronaut-http-client:4.9.4")
}

application {
    mainClass = "org.charitable.app.Application"
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

graalvmNative.toolchainDetection = false

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.8"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.73.0"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
            }
        }
    }
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("org.charitable.app.*")
    }
    aot {
        optimizeServiceLoading = false
        convertYamlToJava = false
        precomputeOperations = true
        cacheEnvironment = true
        optimizeClassLoading = true
        deduceEnvironment = true
        optimizeNetty = true
        replaceLogbackXml = true
    }
}

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}

tasks.register<JavaExec>("runSeeder") {
    group = "application"
    description = "Runs the seeder command"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass = "org.charitable.app.infrastructure.command.SeedCommand"
//    args("seed-admin")
}