package com.david.springkafkaproducer.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import io.confluent.develope.Customer1;

import java.util.List;

@Component
public class CustomerSender {

    private static final Logger log = LoggerFactory.getLogger(CustomerSender.class);

    @Autowired
    KafkaTemplate<Long, Customer1> kafkaTemplate;

    @Value("${customer-topic}")
    private String topic;

    public void sendCustomerEvent(Customer1 customer) {
        final Long key = customer.getId();

        ProducerRecord<Long,Customer1> producerRecord = buildProducerRecord(key, customer, topic);


        ListenableFuture<SendResult<Long, Customer1>> future =
                kafkaTemplate.send(producerRecord);

        future.addCallback(new ListenableFutureCallback<SendResult<Long, Customer1>>() {
            @Override
            public void onFailure(Throwable ex) {
                CustomerSender.this.handleFailure( ex);
            }

            @Override
            public void onSuccess(SendResult<Long, Customer1> result) {
                CustomerSender.this.handleSuccess(result);
            }
        });
    }

    private ProducerRecord<Long, Customer1> buildProducerRecord(Long key, Customer1 value, String topic) {


        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<>(topic, null, null, key, value, recordHeaders);
    }

    private void handleFailure(Throwable ex) {
        log.error("Error Sending the Message and the exception is {}", ex.getMessage());

        try {
            throw ex;
        } catch (Throwable var5) {
            log.error("Error in OnFailure: {}", var5.getMessage());
        }
    }

    private void handleSuccess(SendResult<Long, Customer1> result) {
        log.info("Message Sent SuccessFully for the result : }", result.toString());
    }
}
