package producer.partitioner

import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster
import org.apache.kafka.common.InvalidRecordException
import org.apache.kafka.common.PartitionInfo
import org.apache.kafka.common.utils.Utils

class CustomPartitioner(): Partitioner {
    override fun configure(configs: MutableMap<String, *>?) {
    }

    override fun close() {
    }

    override fun partition(
        topic: String?,
        key: Any?,
        keyBytes: ByteArray?,
        value: Any?,
        valueBytes: ByteArray?,
        cluster: Cluster?
    ): Int {
        if(keyBytes == null) {
            throw InvalidRecordException("Need message key")
        }

        if(key?.equals("Pankyo") == true) {
            return 0
        }

        //hash value
        val partitions: List<PartitionInfo> = cluster!!.partitionsForTopic(topic)
        val numPartitions: Int = partitions.size
        return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions
    }

}