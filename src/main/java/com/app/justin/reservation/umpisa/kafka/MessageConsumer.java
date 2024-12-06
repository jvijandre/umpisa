package com.app.justin.reservation.umpisa.kafka;

import com.app.justin.reservation.umpisa.service.EmailService;
import com.app.justin.reservation.umpisa.service.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageConsumer {

    private final EmailService emailService;

    private final SMSService smsService;

    @KafkaListener(topics = "sms", groupId = "group_id")
    public void consumeSMSNotification(String message) throws InterruptedException {
        smsService.notify(message);
    }

    @KafkaListener(topics = "email", groupId = "group_id")
    public void consumeEmailNotification(String message) throws InterruptedException {
        emailService.notify(message);
    }
}
