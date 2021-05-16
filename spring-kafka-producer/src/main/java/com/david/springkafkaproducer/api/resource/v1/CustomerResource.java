package com.david.springkafkaproducer.api.resource.v1;

import com.david.springkafkaproducer.domain.entity.Customer;
import com.david.springkafkaproducer.domain.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/customers")
public class CustomerResource {

    private CustomerService customerService;

    @PostMapping
    public void save(@RequestBody Customer customer) {
        this.customerService.sendMensage(customer);
    }
}
