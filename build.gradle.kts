import java.util.*
plugins {
    `java-library`
    `maven-publish`
}

group = "me.wawwior"
version = "1.3.8"

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
