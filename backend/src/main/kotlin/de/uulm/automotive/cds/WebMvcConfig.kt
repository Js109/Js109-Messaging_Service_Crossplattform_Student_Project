package de.uulm.automotive.cds

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.*

@Configuration
/**
 * Configuration for overriding the String to Array<String> conversion behavior for @RequestParam.
 * A single String used to be split into several Strings by commas, now a Array<String> containing only the one String is created instead.
 */
class WebMvcConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        super.addFormatters(registry)
        registry.removeConvertible(String::class.java, Array<String>::class.java)
        registry.addConverter(String::class.java, Array<String>::class.java) { arrayOf(it) }
    }
}