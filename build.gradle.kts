import java.util.*
plugins {
    `java-library`
    `maven-publish`
}

group = "me.wawwior"
version = "1.3.8"

val credentials: Properties by lazy {
    Properties().apply {
        file("credentials.properties").inputStream().use {
            load(it)
        }
    }
}

val mavenUser: String = credentials.getProperty("username")
val mavenPassword: String = credentials.getProperty("password")

repositories {
    mavenCentral()
}

dependencies {

    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.yaml:snakeyaml:1.33")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "maven"
            url = uri("https://repo.wawwior.me/repository/maven-releases/")
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
    }
}