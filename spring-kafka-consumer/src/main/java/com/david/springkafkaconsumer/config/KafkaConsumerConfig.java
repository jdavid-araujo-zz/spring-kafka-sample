package com.david.springkafkaconsumer.config;

import com.david.springkafkaconsumer.kafka.exceptions.CustomerKafkaConsumerErrorHandler;
import com.david.springkafkaconsumer.kafka.exceptions.CustomerKafkaConsumerRecovery;
import io.confluent.develope.Customer1;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;


@Profile("dev")
@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    private final Integer KAFKA_CONSUMER_MAX_RETRY_POLICY = 3;

    private final Long KAFKA_CONSUMER_RETRY_BACK_OFF_PERIOD = 1000L;

    private final Integer KAFKA_CONSUMER_CONCURRENCY = 3;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                KafkaAvroDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest");
        props.put("schema.registry.url",
                "http://localhost:8001/api/schema-registry");

        return props;
    }

    @Bean
    public ConsumerFactory<Long, Customer1> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Long, Customer1>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, Customer1> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(KAFKA_CONSUMER_CONCURRENCY);
        factory.setRetryTemplate(retryTemplate());
        factory.setErrorHandler(new CustomerKafkaConsumerErrorHandler());
        factory.setRecoveryCallback(new CustomerKafkaConsumerRecovery());

        return factory;
    }

    private RetryTemplate retryTemplate() {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(KAFKA_CONSUMER_RETRY_BACK_OFF_PERIOD);
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(simpleRetryPolicy());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        return  retryTemplate;
    }

    private RetryPolicy simpleRetryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionsMap = new HashMap<>();
        exceptionsMap.put(IllegalArgumentException.class, false);
        exceptionsMap.put(RecoverableDataAccessException.class, true);
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(KAFKA_CONSUMER_MAX_RETRY_POLICY,exceptionsMap,true);
        return simpleRetryPolicy;
    }
}
