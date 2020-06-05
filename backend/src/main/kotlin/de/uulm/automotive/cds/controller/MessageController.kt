package de.uulm.automotive.cds.controller

import com.rabbitmq.client.AMQP.BasicProperties
import de.uulm.automotive.cds.models.CategoryRepository
import de.uulm.automotive.cds.models.Message
import de.uulm.automotive.cds.services.AmqpChannelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*

@RestController
class MessageController @Autowired constructor(private val amqpChannelService: AmqpChannelService, private val categoryRepository: CategoryRepository){

    @GetMapping("/message")
    fun messageForm(model: Model): String {
        model["title"] = "Message"
        return "message"
    }

    @PostMapping("/message")
    fun relayMessage(@RequestBody message: Message) {


        val bindingArgs = HashMap<String, Any>()

        for(id in message.categoryIds) {
            val category = categoryRepository.findById(id)
            category.ifPresent {
                for(binding in it.bindings) {
                    bindingArgs[binding] = ""
                }
            }
        }

        var props = BasicProperties()
        props = props.builder().headers(bindingArgs).build()

        val channel = amqpChannelService.openChannel()

        channel.basicPublish("amq.direct", "/test/direct", null, message.content.toByteArray())

        channel.close()
    }
}