package de.uulm.automotive.cds

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * The application class for the spring server.
 * The necessary properties (auto configuration, component scan...) are set via the @SpringBootApplication annotation.
 *
 */
@SpringBootApplication
@EnableScheduling
class CdsApplication

/**
 * The main class starts the server by running the application class
 *
 * @param args
 */
fun main(args: Array<String>) {
    runApplication<CdsApplication>(*args)
}
