package com.david.springkafkaproducer.domain.service;

import com.david.springkafkaproducer.domain.entity.Customer;
import com.david.springkafkaproducer.kafka.CustomerSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.confluent.develope.Customer1;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomerService {

    private CustomerSender customerSender;

    public void sendMensage(Customer customer) {
        this.customerSender.sendCustomerEvent(new Customer1(customer.getId(), customer.getName()));
    }
}

