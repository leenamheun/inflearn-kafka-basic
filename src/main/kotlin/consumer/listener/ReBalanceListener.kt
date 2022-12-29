package consumer.listener

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener
import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ReBalanceListener")

class ReBalanceListener(): ConsumerRebalanceListener {
    override fun onPartitionsRevoked(partitions: MutableCollection<TopicPartition>?) {
        logger.warn("Partitions are assigned");
    }

    override fun onPartitionsAssigned(partitions: MutableCollection<TopicPartition>?) {
        logger.warn("Partitions are revoked");
    }
}