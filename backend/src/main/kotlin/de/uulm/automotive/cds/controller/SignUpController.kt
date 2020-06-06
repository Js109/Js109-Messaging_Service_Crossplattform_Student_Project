package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.models.SignUpInfo
import de.uulm.automotive.cds.services.AmqpChannelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SignUpController @Autowired constructor(private val amqpService: AmqpChannelService){
    @PostMapping("/signup")
    fun testResource(@RequestBody info: SignUpInfo) {
        val channel = amqpService.openChannel()
        channel.queueDeclare("id/${info.id}", true, false, false, null)
        val headersMap = HashMap<String, Any>()
        headersMap["x-match"] = "any"
        headersMap["id/${info.id}"] = ""
        headersMap["device/${info.deviceType}"] = ""
        channel.queueBind("id/${info.id}", "amq.headers", "", headersMap)
        channel.close()
    }
}