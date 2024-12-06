package com.app.justin.reservation.umpisa.service;

import com.app.justin.reservation.umpisa.service.base.NotificationMessageQueue;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmailService implements NotificationMessageQueue {
    @Override
    public void notify(String message) {
        //Add logic here to send message via Email
        // Can add delay for 3 seconds
        // Thread.sleep(3000);
        log.info(message);
        log.info("Sent EMAIL");
    }
}
