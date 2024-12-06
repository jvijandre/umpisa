package com.app.justin.reservation.umpisa.scheduler;

import com.app.justin.reservation.umpisa.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationService reservationService;

    //Runs every hour
    @Scheduled(cron = "0 * * * * ?") //Comment this out if you will test below

    //Use this and comment out above to check if it notifies every 3 seconds
    //@Scheduled(fixedDelay = 3000, initialDelay = 5000)
    public void scheduleTask() {
        reservationService.sendReminder();
    }
}
