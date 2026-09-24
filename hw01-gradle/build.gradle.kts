plugins {
    id("java")
}

group = "ru.otus.java.pro"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.6.0-jre")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("org.postgresql:postgresql:42.7.7")
    implementation("org.flywaydb:flyway-core:11.13.2")
    implementation("org.flywaydb:flyway-database-postgresql:11.13.2")
    implementation("org.hibernate.orm:hibernate-core:6.6.29.Final")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runHomeWork") {
    group = "application"
    description = "Runs the hw09 homemade ORM demo"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("ru.otus.java.pro.hw09.HomeWork")
}

tasks.register<JavaExec>("runHw10") {
    group = "application"
    description = "Runs the hw10 Hibernate demo"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("ru.otus.java.pro.hw10.DbServiceDemo")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.otus.java.pro.Main"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    val dependencies = configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    from(dependencies)
}
