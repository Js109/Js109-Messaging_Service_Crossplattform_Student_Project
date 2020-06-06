package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.models.CategoryRepository
import de.uulm.automotive.cds.models.Subscription
import de.uulm.automotive.cds.services.AmqpChannelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SubscribeController @Autowired constructor(private val amqpService: AmqpChannelService, private val categoryRepository: CategoryRepository){
    @PostMapping("/subscriptions")
    fun postSubscriptions(@RequestBody subscription: Subscription) {
        val category = categoryRepository.findById(subscription.categoryId)
        category.ifPresent {
            val channel = amqpService.openChannel()
            val bindingArgs = HashMap<String, Any>()
            bindingArgs["x-match"] = "any"
            for (binding in it.bindings) {
                bindingArgs[binding] = ""
            }
            channel.queueBind("id/${subscription.userId}", "amq.headers", "", bindingArgs)
            channel.close()
        }
    }
}