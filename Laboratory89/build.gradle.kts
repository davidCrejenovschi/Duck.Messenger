plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.postgresql:postgresql:42.7.8")
    testImplementation("com.h2database:h2:2.2.224")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainModule.set("project.duckman")
    mainClass.set("graphical.Launcher")
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}

javafx {
    version = "21"
    modules = listOf(
        "javafx.controls",
        "javafx.fxml",
        "javafx.graphics",
        "javafx.base"
    )
}


tasks.named<JavaExec>("run") {
    jvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}
