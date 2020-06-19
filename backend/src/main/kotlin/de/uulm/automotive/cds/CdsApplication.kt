package de.uulm.automotive.cds

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * TODO
 *
 */
@SpringBootApplication
@EnableScheduling
class CdsApplication

/**
 * TODO
 *
 * @param args
 */
fun main(args: Array<String>) {
    runApplication<CdsApplication>(*args)
}
