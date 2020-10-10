package de.uulm.automotive.cds.services


import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * A service class that creates a connection to the amqp broker and allows the opening of communication channels to that broker.
 * Other components can use this one to communicate with the broker.
 */
@Component
class AmqpChannelService(
        @Value("\${amq.broker.url}") private val address: String,
        @Value("\${amq.broker.username}") private val username: String,
        @Value("\${amq.broker.password}") private val password: String
) {

    private val connection: Connection

    init {
        val factory = ConnectionFactory()
        factory.host = address
        factory.username = username
        factory.password = password
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