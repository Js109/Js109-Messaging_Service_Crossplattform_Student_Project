package de.uulm.automotive.cds.services


import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.ConnectException

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

    companion object {
        val logger: Logger = LoggerFactory.getLogger(AmqpChannelService::class.java)
    }

    private val connection: Connection? = null
    private val factory =
            ConnectionFactory()
                    .apply {
                        this.host = this@AmqpChannelService.address
                        this.username = this@AmqpChannelService.username
                        this.password = this@AmqpChannelService.password
                    }
    /**
     * Opens a new channel to the amqp broker.
     * When the communication with the broker is done Channel.close() should be called.
     * @return Channel for communication with the amqp broker
     */
    fun openChannel(): Channel? = try {
        (connection ?: factory.newConnection())
                .createChannel()
    } catch (exception: ConnectException) {
        logger.error("Can not connect to Broker.")
        null
    }
}

