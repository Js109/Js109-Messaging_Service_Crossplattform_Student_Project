package de.uulm.automotive.cds.services


import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import org.springframework.stereotype.Component

@Component
class AmqpChannelService() {

    private val connection: Connection

    init {
        val factory = ConnectionFactory()
        factory.host = "134.60.157.15"
        factory.username = "android_cl"
        factory.password = "supersecure"
        connection = factory.newConnection()
    }

    /**
     * Opens a new channel to the amqp broke.
     * When the communication with the broker is done Channel.close() should be called.
     * @return Channel for communication with the amqp broker
     */
    fun openChannel(): Channel {
        return connection.createChannel()
    }
}