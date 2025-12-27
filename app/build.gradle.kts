plugins {
    scala
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.scala.library)
    implementation(libs.guava)

    testImplementation(libs.scalatest)
    testRuntimeOnly(libs.scala.xml)
    testRuntimeOnly("org.scala-lang.modules:scala-xml_3:2.3.0")
    testRuntimeOnly("org.scalatestplus:junit-5-10_3:3.2.19.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        includeEngines("scalatest")
    }
}

application {
    mainClass = "pl.waw.oledzki.signpost.App"
}
