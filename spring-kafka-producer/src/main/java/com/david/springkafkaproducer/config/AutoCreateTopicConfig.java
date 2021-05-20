package com.david.springkafkaproducer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoCreateTopicConfig {

    @Value("${customer.topic}")
    private String customerTopic;



    public AutoCreateTopicConfig() {
    }

    @Bean
    public NewTopic libraryEvents() {
        return TopicBuilder.name(customerTopic).partitions(3).replicas(1).build();
    }


}
