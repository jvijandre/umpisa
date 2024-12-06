package com.app.justin.reservation.umpisa.kafka;

import com.app.justin.reservation.umpisa.exception.ReservationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) throws ReservationException {
        try {
            //Added blocking 3 second waiting time.
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            throw new ReservationException("Kafka Error.");
        }
    }
}
