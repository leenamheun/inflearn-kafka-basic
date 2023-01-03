package streams

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.processor.Processor
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.processor.ProcessorSupplier
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

    val topology = Topology()
    topology.addSource("Source", STREAM_LOG)
        .addProcessor("Process", ProcessorSupplier { FilterProcessor() }, "Source")
        .addSink("Sink", STREAM_LOG_FILTER, "Process")

    val streams = KafkaStreams(topology, props)
    streams.start()
}

open class FilterProcessor : Processor<String, String> {
    private lateinit var context: ProcessorContext

    override fun init(context: ProcessorContext?) {
        this.context = context!!
    }

    override fun close() {
    }

    override fun process(key: String?, value: String?) {
        if (value!!.length > 5) {
            context.forward(key, value)
        }
        context.commit()
    }
}