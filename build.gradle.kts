plugins {
	java
	`java-library`
	id("io.spring.dependency-management") version "1.1.7"
	`maven-publish`
}

group = "com.control"
version = "1.0.28"
description = "Core Authentication and User Management Spring Boot Starter"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
	withSourcesJar()
	withJavadocJar()
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.3")
	}
}

dependencies {
	// Spring Boot Starters - API dependencies (will be transitive)
	api("org.springframework.boot:spring-boot-starter-web")
	api("org.springframework.boot:spring-boot-starter-security")
	api("org.springframework.boot:spring-boot-starter-data-jpa")
	api("org.springframework.boot:spring-boot-starter-thymeleaf")
	api("org.springframework.boot:spring-boot-starter-validation")
	api("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
	
	// Optional email support - consuming apps must add this if they want email functionality
	compileOnly("org.springframework.boot:spring-boot-starter-mail")
	
	// Configuration processor for IDE support
	compileOnly("org.springframework.boot:spring-boot-configuration-processor")
	
	// Optional dependencies - let consuming apps choose their database
	compileOnly("org.postgresql:postgresql")
	compileOnly("com.h2database:h2")
	compileOnly("org.springframework.boot:spring-boot-starter-actuator")
	
	// Development and Test dependencies
	compileOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-mail") // For testing
	testImplementation("org.springframework.boot:spring-boot-starter-actuator") // For testing AdminController
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("-parameters")
}

// Configure the plain jar task
tasks.withType<Jar> {
	// Exclude the main application class since this is a library
	exclude("**/CoreApplication.class")
	exclude("**/CoreApplication*.class")
	// Exclude application configuration files that are specific to the main app
	exclude("application.properties")
	exclude("data.sql")
	exclude("application-*.properties")
	// Exclude index.html as consumer apps should handle their own root routes
	exclude("templates/index.html")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
			
			groupId = project.group.toString()
			artifactId = "core-auth-starter"
			version = project.version.toString()
			
			// Resolve dependency versions
			versionMapping {
				usage("java-api") {
					fromResolutionOf("runtimeClasspath")
				}
				usage("java-runtime") {
					fromResolutionResult()
				}
			}
			
			pom {
				name.set("Core Auth Starter")
				description.set("A comprehensive Spring Boot Starter for authentication and user management with Material UI frontend. Features include login/logout, user registration, password reset, admin dashboard, role-based access control, and multi-provider email support.")
				url.set("https://github.com/papapitufo/core")
				
				licenses {
					license {
						name.set("MIT License")
						url.set("https://opensource.org/licenses/MIT")
					}
				}
				
				developers {
					developer {
						id.set("papapitufo")
						name.set("Rob Moller")
						email.set("robimoller@example.com")
					}
				}
				
				scm {
					connection.set("scm:git:git://github.com/papapitufo/core.git")
					developerConnection.set("scm:git:ssh://github.com/papapitufo/core.git")
					url.set("https://github.com/papapitufo/core")
				}
			}
		}
	}
	
	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/papapitufo/core")
			credentials {
				username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
				password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
			}
		}
	}
}
