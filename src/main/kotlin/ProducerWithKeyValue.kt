import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.*

private val TOPIC_NAME = "test"
private val BOOTSTRAP_SERVERS = "my-kafka:9092"

fun main(args: Array<String>) {
    val configs = Properties()
    configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name

    val producer = KafkaProducer<String, String>(configs)

    val record1 = ProducerRecord(TOPIC_NAME, "Pankyo", "판교")
    producer.send(record1)
    val record2 = ProducerRecord(TOPIC_NAME, "Busan", "부산")
    producer.send(record2)

    producer.flush()
    producer.close()
}