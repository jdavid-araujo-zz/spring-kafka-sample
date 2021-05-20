package com.david.springkafkaconsumer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import io.confluent.develope.Customer1;

@Component
@Slf4j
public class CustomerEventsConsumer {

    @KafkaListener(topics = "${customer.topic}", groupId = "${customer.group-id}")
    public void onMessage(ConsumerRecord<Long,Customer1> consumerRecord)  {

        log.info("ConsumerRecord : {} ", consumerRecord );
    }
}
