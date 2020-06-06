package de.uulm.automotive.cds

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class CdsApplication

fun main(args: Array<String>) {
    runApplication<CdsApplication>(*args)
}
