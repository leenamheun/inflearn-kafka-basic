import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.*

private val logger = LoggerFactory.getLogger("SimpleProducer")
private val TOPIC_NAME = "test"
private val BOOTSTRAP_SERVERS = "my-kafka:9092"

fun main(args: Array<String>) {
    //set essential options
    val configs = Properties()
    configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name

    //create kafka producer
    val producer = KafkaProducer<String, String>(configs)

    //create record
    val messageValue = "hi~ this is test message!"
    val record = ProducerRecord<String, String>(TOPIC_NAME, messageValue)

    //send
    producer.send(record)

    logger.info("{}", record)

    producer.flush()
    producer.close()
}