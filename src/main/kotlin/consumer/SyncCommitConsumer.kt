package consumer

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*
import kotlin.collections.HashMap

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

    /* 자동 커밋 ON
    configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
    configs[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = 60000
    */

    // 수동커밋
    configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false

    val consumer = KafkaConsumer<String, String>(configs)

    consumer.subscribe(listOf(TOPIC_NAME))

    while (true) {
        val records = consumer.poll(Duration.ofSeconds(1))
        val currentOffset = HashMap<TopicPartition, OffsetAndMetadata>()
        records.forEach { record ->
            logger.info("record: {}", record)
            /* 레코드 단위로 커밋을 진행 -> 계속 브로커와 통신하기 때문에 처리속도가 낮다
            currentOffset[TopicPartition(record.topic(), record.partition())] =
                OffsetAndMetadata(record.offset()+1, null)
            consumer.commitSync(currentOffset)
            */
        }
        consumer.commitSync()
    }
}