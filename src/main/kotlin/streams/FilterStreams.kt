package streams

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import java.util.*

private val APPLICATION_NAME = "streams-filter-application"
private val BOOTSTRAP_SERVERS = "my-kafka:9092"
private val STREAM_LOG = "stream_log"
private val STREAM_LOG_FILTER = "stream_log_filter"

fun main() {
    val props = Properties()
    props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    props[StreamsConfig.APPLICATION_ID_CONFIG] = APPLICATION_NAME
    props[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
    props[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass

    val builder = StreamsBuilder()
    val streamLog = builder.stream<String, String>(STREAM_LOG)

    streamLog.filter { _, value -> value.length > 5 }.to(STREAM_LOG_FILTER)

    val streams = KafkaStreams(builder.build(), props)
    streams.start()
}

