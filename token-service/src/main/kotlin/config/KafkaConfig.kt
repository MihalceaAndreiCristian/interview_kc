import org.apache.camel.component.kafka.KafkaComponent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces

@ApplicationScoped
class KafkaConfig {

    @Produces
    fun kafkaComponent(): KafkaComponent {
        val kafka = KafkaComponent()
        kafka.configuration.brokers = "localhost:9092"
        return kafka
    }
}
