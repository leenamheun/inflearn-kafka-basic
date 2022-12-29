package consumer

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

private val logger = LoggerFactory.getLogger("SyncCommitConsumer")
private val TOPIC_NAME = "test"
private val BOOTSTRAP_SERVERS = "my-kafka:9092"
private val GROUP_ID = "test-group"

fun main(args: Array<String>) {
    val configs = Properties()
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
    configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
    configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
    configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false

    val consumer = KafkaConsumer<String, String>(configs)

    consumer.subscribe(listOf(TOPIC_NAME))

    while (true) {
        val records = consumer.poll(Duration.ofSeconds(1))
        records.forEach { record ->
            logger.info("record: {}", record)
        }
        consumer.commitAsync { offsets, exception ->
            if (exception != null) {
                System.err.println("Commit failed")
            } else {
                println("Commit Succeeded")
            }

            if (exception != null) {
                logger.error("Commit failed for offsets {}", offsets, exception)
            }
        }
        //consumer.commitAsync()
    }
}