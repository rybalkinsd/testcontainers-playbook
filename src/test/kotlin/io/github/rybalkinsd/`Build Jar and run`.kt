package io.github.rybalkinsd

import io.github.rybalkinsd.util.KGenericContainer
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.output.WaitingConsumer
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy
import org.testcontainers.images.builder.ImageFromDockerfile
import java.io.File


/**
 *
 * Build jar with Main class:
 *
 *  public static void main(String[] args) {
 *      System.out.println("Hello, " + System.getenv("param"));
 *  }
 *
 * According to Dockerfile:
 *
 *  FROM openjdk:8u181-jdk-alpine3.8
 *  COPY ["pack", "."]
 *  RUN ["javac", "io/github/rybalkinsd/Main.java"]
 *  RUN ["jar", "cfm", "Main.jar", "MANIFEST.MF", "io/github/rybalkinsd/Main.class"]
 *  CMD ["java", "-jar", "Main.jar"]
 *
 */
class JavaBuildTest {

    lateinit var container: KGenericContainer

    @Before
    fun setUp() {
        container = KGenericContainer(
            ImageFromDockerfile()
                .withDockerfileFromBuilder { builder ->
                    builder.from("openjdk:8u181-jdk-alpine3.8")
                        .copy("/pack", ".")
                        .run("javac", "io/github/rybalkinsd/Main.java")
                        .run("jar", "cfm", "Main.jar", "MANIFEST.MF", "io/github/rybalkinsd/Main.class")
                        .cmd("java", "-jar", "Main.jar")
                        .build()
                }.withFileFromFile("/pack", File("./pack"))
        )
    }

    @Test
    fun `system env access test`() {
        val logger = LoggerFactory.getLogger("container")
        val wait = WaitingConsumer()

        container
            .withEnv("param", "System env")
            .withStartupCheckStrategy(OneShotStartupCheckStrategy())
            .withLogConsumer(wait.andThen(Slf4jLogConsumer(logger)))
            .start()

        wait.waitUntilEnd()
    }
}
