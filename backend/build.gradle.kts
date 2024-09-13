plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}
group = "app.vcampus"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.hibernate.orm:hibernate-core:6.2.7.Final")
    implementation("io.netty:netty-all:4.1.97.Final")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.sun.mail:javax.mail:1.6.2")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.65")

}
tasks.test {
    useJUnitPlatform()

}
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "app.vcampus.Main"
    }
    isZip64 = true
    exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
    doFirst {
        from(configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        })

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}