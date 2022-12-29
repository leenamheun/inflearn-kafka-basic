package consumer.listener

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

private val logger = LoggerFactory.getLogger("PartitionConsumer")
private val TOPIC_NAME = "test"
private val BOOTSTRAP_SERVERS = "my-kafka:9092"
private val PARTITION_NUMBER = 0

fun main(args: Array<String>) {
    val configs = Properties()
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
    configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name

    val consumer = KafkaConsumer<String, String>(configs)
    consumer.assign(Collections.singleton(TopicPartition(TOPIC_NAME, PARTITION_NUMBER)))

    while (true) {
        val records = consumer.poll(Duration.ofSeconds(1))
        records.forEach { record ->
            logger.info("record: {}", record)
        }
    }
}