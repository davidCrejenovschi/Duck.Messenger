plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.postgresql:postgresql:42.7.8")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainModule.set("project.duckman")
    mainClass.set("ui.graphical.Launcher")
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}

javafx {
    version = "25"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics", "javafx.base")
}

tasks.named<JavaExec>("run") {
    jvmArgs = listOf("--enable-native-access=javafx.graphics")
}
