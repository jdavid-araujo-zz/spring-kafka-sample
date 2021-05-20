package com.david.springkafkaconsumer.kafka.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryContext;

@Slf4j
public class CustomerKafkaConsumerRecovery implements RecoveryCallback<RecoverableDataAccessException> {
    @Override
    public RecoverableDataAccessException recover(RetryContext context) throws Exception {
        log.info("RecoverableDataAccessException: {}", context.toString());

        return null;
    }
}
