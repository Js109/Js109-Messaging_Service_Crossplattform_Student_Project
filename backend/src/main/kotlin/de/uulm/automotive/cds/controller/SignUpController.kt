package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.models.SignUpInfo
import de.uulm.automotive.cds.services.AmqpChannelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

/**
 * TODO
 *
 * @property amqpService
 */
@RestController
class SignUpController @Autowired constructor(private val amqpService: AmqpChannelService, private val tokenRepository: SignUpRepository) {
    @PostMapping("/signup")
    fun testResource(@RequestBody info: SignUpInfo): UUID? {
        // check if the Token is already saved in the Database
        val signUpToken = tokenRepository.findBySignUpToken(info.signUpToken)
        if (signUpToken == null) {
            // generate new unique identifier for the client and return it to the client
            val id = UUID.randomUUID()

            val channel = amqpService.openChannel()
            channel.queueDeclare("id/${id}", true, false, false, null)
            val headersMap = HashMap<String, Any>()
            headersMap["x-match"] = "any"
            headersMap["id/${id}"] = ""
            headersMap["device/${info.deviceType}"] = ""
            channel.queueBind("id/${id}", "amq.headers", "", headersMap)
            channel.close()

            return id
        } else {
            // update time of the last use of the token
            signUpToken.timeLastUsed = LocalDateTime.now()
            tokenRepository.save(signUpToken)
            return null
        }
    }
}