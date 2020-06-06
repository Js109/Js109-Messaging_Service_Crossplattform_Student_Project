package de.uulm.automotive.cds

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {

    @Bean
    fun databaseInitializer(messageRepository: MessageRepository) = ApplicationRunner {

    }
}