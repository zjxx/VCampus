plugins {
    id("java")
}

group = "app.vcampus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.hibernate.orm:hibernate-core:6.2.7.Final")
    implementation("io.netty:netty-all:4.1.97.Final")
    implementation("com.google.code.gson:gson:2.10.1")

}

tasks.test {
    useJUnitPlatform()

}