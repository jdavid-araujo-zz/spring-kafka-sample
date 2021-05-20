package com.david.springkafkaproducer.domain.service;

import com.david.springkafkaproducer.domain.entity.Customer;
import com.david.springkafkaproducer.kafka.CustomerSender;
import io.confluent.develope.CustomerAVRO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomerService {

    private CustomerSender customerSender;

    public void sendMensage(Customer customer) {
        this.customerSender.sendCustomerEvent(new CustomerAVRO(customer.getId(), customer.getName()));
    }
}

