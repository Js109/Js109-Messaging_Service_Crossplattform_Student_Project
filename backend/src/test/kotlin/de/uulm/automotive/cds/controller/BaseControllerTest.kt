package de.uulm.automotive.cds.controller

import com.ninjasquad.springmockk.MockkBean
import de.uulm.automotive.cds.repositories.*
import de.uulm.automotive.cds.services.AmqpChannelService
import de.uulm.automotive.cds.services.MessageService
import de.uulm.automotive.cds.services.MetricsService

/**
 * A WebMvcTest creates all Controllers defined in the project.
 * For this to work all Repositories/Services that are included in any Controller via @autowired must be initialized in the test.
 * This base class was created so that these fields only need to be defined once.
 *
 * Extending this class allows access to all repositories/services as class fields.
 */
open class BaseControllerTest {
    @MockkBean
    protected lateinit var messageRepository: MessageRepository

    @MockkBean
    protected lateinit var propertyRepository: PropertyRepository

    @MockkBean
    protected lateinit var topicRepository: TopicRepository

    @MockkBean
    protected lateinit var signUpRepository: SignUpRepository

    @MockkBean
    protected lateinit var templateRepository: TemplateRepository

    @MockkBean
    protected lateinit var messageService: MessageService

    @MockkBean
    protected lateinit var metricsService: MetricsService

    @MockkBean
    protected lateinit var amqpChannelService: AmqpChannelService
}