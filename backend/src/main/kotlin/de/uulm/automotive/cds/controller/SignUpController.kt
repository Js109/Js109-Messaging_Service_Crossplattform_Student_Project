package de.uulm.automotive.cds.controller

import de.uulm.automotive.cds.entities.Token
import de.uulm.automotive.cds.models.SignUpInfo
import de.uulm.automotive.cds.models.dtos.TokenDTO
import de.uulm.automotive.cds.repositories.SignUpRepository
import de.uulm.automotive.cds.services.AmqpChannelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

/**
 * Rest-Endpoint for SignUp of new clients.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/signup")
class SignUpController @Autowired constructor(private val amqpService: AmqpChannelService, private val tokenRepository: SignUpRepository) {

    /**
     * Checks if the client has already tried to register with the given SignUp info and either returns
     * a new QueueID if the client did not register already or the saved QueueID if the client did
     * register.
     *
     * @param info SignUp Info of the client
     * @return TokenDTO with the signUp Token and the QueueID of the client
     */
    @PostMapping
    fun signUp(@RequestBody info: SignUpInfo): TokenDTO {
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
            headersMap["property/device/${info.deviceType}"] = ""
            channel.queueBind("id/${id}", "amq.headers", "", headersMap)
            channel.close()

            val token = Token(info.signUpToken, id, LocalDateTime.now())
            tokenRepository.save(token)

            return TokenDTO.toDTO(token)
        } else {
            // update time of the last use of the token
            signUpToken.timeLastUsed = LocalDateTime.now()
            tokenRepository.save(signUpToken)
            return TokenDTO.toDTO(signUpToken)
        }
    }
}