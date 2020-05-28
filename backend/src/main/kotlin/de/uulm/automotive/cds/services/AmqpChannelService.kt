package de.uulm.automotive.cds.services


import com.rabbitmq.client.Channel
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.Connection
import org.springframework.stereotype.Component

@Component
class AmqpChannelService() {

    private val connection: Connection

    init {
        val factory = CachingConnectionFactory()
        factory.host = "134.60.157.15"
        factory.username = "android_cl"
        factory.setPassword("supersecure")
        connection = factory.createConnection()
    }

    fun openChannel(): Channel {
        return connection.createChannel(true)
    }
}