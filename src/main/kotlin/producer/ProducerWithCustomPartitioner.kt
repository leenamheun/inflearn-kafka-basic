package partitioner

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import producer.partitioner.CustomPartitioner
import java.util.*

private val TOPIC_NAME = "test"
private val BOOTSTRAP_SERVERS = "my-kafka:9092"

fun main(args: Array<String>) {
    val configs = Properties()
    configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    configs[ProducerConfig.PARTITIONER_CLASS_CONFIG] = CustomPartitioner::class.java

    val producer = KafkaProducer<String, String>(configs)

    val record = ProducerRecord(TOPIC_NAME, "Pankyo", "pankyo")

    producer.send(record)

    producer.flush()
    producer.close()
}