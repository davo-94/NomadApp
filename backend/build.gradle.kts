plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "cl.vasquez.nomadapp"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

// Repositorios configurados en settings.gradle.kts

dependencies {
    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    
    // Oracle Database
    implementation("com.oracle.database.jdbc:ojdbc11:23.2.0.0")
    implementation("com.oracle.database.security:oraclepki:23.2.0.0")
    
    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
    
    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    
    // Validation
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    
    // JSON
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Apache Commons (para evitar conflictos de versión)
    implementation("org.apache.commons:commons-compress:1.23.0")
    
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
