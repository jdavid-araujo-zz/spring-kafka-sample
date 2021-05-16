package com.david.springkafkaconsumer.kafka.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerKafkaConsumerErrorHandler implements ErrorHandler {

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> data) {
        log.error("error: {} data: {}", thrownException.getMessage(), data.toString() );
    }
}
