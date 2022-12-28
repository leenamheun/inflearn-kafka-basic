import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.*

private val logger = LoggerFactory.getLogger("ProducerSyncCallback")
private val TOPIC_NAME = "test"
private val BOOTSTRAP_SERVERS = "my-kafka:9092"

fun main(args: Array<String>) {
    val configs = Properties()
    configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    configs[ProducerConfig.ACKS_CONFIG] = "0"

    val producer = KafkaProducer<String, String>(configs)

    val record = ProducerRecord(TOPIC_NAME, "Pankyo", "Pankyo")

    try {
        val metadata = producer.send(record).get()
        logger.info(metadata.toString())
    } catch (e: Exception) {
        logger.error(e.message)
    } finally {
        producer.flush()
        producer.close()
    }
}