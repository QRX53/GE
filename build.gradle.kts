plugins {
    id("java")
}

group = "org.acmanager"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.github.fragland:MineStat:3.0.0")
    implementation("com.github.tomas-langer:chalk:1.0.0")
}

tasks.test {
    useJUnitPlatform()
}