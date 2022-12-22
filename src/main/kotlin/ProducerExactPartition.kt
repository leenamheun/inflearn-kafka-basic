import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

private val TOPIC_NAME = "test"
private val BOOTSTRAP_SERVERS = "my-kafka:9092"

fun main(args: Array<String>) {
    val configs = Properties()
    configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name

    val producer = KafkaProducer<String, String>(configs)

    val partitionNo = 0
    val record = ProducerRecord(TOPIC_NAME, partitionNo, "exactPartitionNo", "exactPartitionNo:$partitionNo")

    producer.send(record)

    producer.flush()
    producer.close()
}