package com.app.justin.reservation.umpisa.controller;

import com.app.justin.reservation.umpisa.dto.ReservationRequest;
import com.app.justin.reservation.umpisa.dto.ReservationResponse;
import com.app.justin.reservation.umpisa.dto.UpdateNotificationRequest;
import com.app.justin.reservation.umpisa.dto.UpdateReservationRequest;
import com.app.justin.reservation.umpisa.exception.BadRequestException;
import com.app.justin.reservation.umpisa.exception.ReservationException;
import com.app.justin.reservation.umpisa.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createReservation(@Valid @RequestBody ReservationRequest reservationRequest) throws ReservationException {
        reservationService.createReservation(reservationRequest);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateReservation(@PathVariable("id") Long id, @RequestBody UpdateReservationRequest updateReservationRequest) throws BadRequestException {
        reservationService.updateReservation(id, updateReservationRequest);
    }

    @PutMapping("/update/notification/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateNotification(@PathVariable("id") Long id, @RequestBody UpdateNotificationRequest updateNotificationRequest) throws ReservationException, BadRequestException {
        reservationService.updateNotification(id, updateNotificationRequest);
    }

    @PutMapping("/cancel/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void cancelReservation(@PathVariable("id") Long id) throws ReservationException, BadRequestException {
        reservationService.cancelReservation(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> viewReservations() {
        return reservationService.viewReservations();
    }
}
