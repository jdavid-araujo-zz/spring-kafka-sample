package com.david.springkafkaproducer.config;

import io.confluent.develope.Customer1;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Profile("dev")
@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.producer.bootstrap-servers}")
    private String bootstrapServers;


    @Bean
    public Map<String, Object> producerDefaultConfiguration() {
        Map<java.lang.String, java.lang.Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                KafkaAvroSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
                "true");
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
                "3");
        props.put(ProducerConfig.ACKS_CONFIG,
                "all");
        props.put(ProducerConfig.RETRIES_CONFIG,
                "3");
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,
                "1000");
        props.put("schema.registry.url",
                "http://localhost:8001/api/schema-registry");
        props.put("admin.properties.bootstrap.servers",
                "localhost:9092");

        return props;
    }

    @Bean
    public ProducerFactory<Long, Customer1> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerDefaultConfiguration());
    }

    @Bean
    public KafkaTemplate<Long, Customer1> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
