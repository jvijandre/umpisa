package com.app.justin.reservation.umpisa.service;

import com.app.justin.reservation.umpisa.dto.ReservationRequest;
import com.app.justin.reservation.umpisa.dto.ReservationResponse;
import com.app.justin.reservation.umpisa.dto.UpdateNotificationRequest;
import com.app.justin.reservation.umpisa.dto.UpdateReservationRequest;
import com.app.justin.reservation.umpisa.exception.BadRequestException;
import com.app.justin.reservation.umpisa.exception.ReservationException;
import com.app.justin.reservation.umpisa.kafka.MessageProducer;
import com.app.justin.reservation.umpisa.model.Reservation;
import com.app.justin.reservation.umpisa.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MessageProducer messageProducer;
    private final EmailService emailService;
    private final SMSService smsService;
    private static final String SMS_TOPIC = "sms";
    private static final String EMAIL_TOPIC = "email";

    public void createReservation(ReservationRequest reservationRequest) throws ReservationException {
        try {
            if(reservationRepository.getExistingReservations(reservationRequest.getName()).isEmpty()) {
                Reservation reservation = new Reservation(
                        null,
                        reservationRequest.getName(),
                        reservationRequest.getContactNumber(),
                        reservationRequest.getEmail(),
                        reservationRequest.getReservationDateTime(),
                        reservationRequest.getNumberOfGuests(),
                        true,
                        true,
                        true);
                reservationRepository.save(reservation);

                sendNotification(
                        "Created " + getReservationMessage(reservationRequest),
                        reservation.isNotifyViaSMS(),
                        reservation.isNotifyViaEmail());
            } else {
                log.error("Existing Active Reservation already exists.");
                throw new ReservationException("Active Reservation already exists.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Create Reservation failed.");
            throw new ReservationException("Create Reservation failed. " + ex.getLocalizedMessage());
        }
    }

    public List<ReservationResponse> viewReservations() {
        return reservationRepository.findAll().stream()
                .map(this::reservationMapper)
                .toList();
    }

    public void updateReservation(Long id, UpdateReservationRequest updateReservationRequest) throws BadRequestException {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if(reservationOptional.isPresent()) {
            reservationRepository.updateTimeAndGuests(
                    updateReservationRequest.getReservationDateTime(),
                    updateReservationRequest.getNumberOfGuests(),
                    id);
            log.info("Reservation Updated!");
        } else {
            log.error("Reservation Not Found!");
            throw new BadRequestException("Error with the specified id.");
        }
    }

    public void updateNotification(Long id, UpdateNotificationRequest updateNotificationRequest) throws ReservationException, BadRequestException {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if(reservationOptional.isPresent()) {
            reservationRepository.updateNotificationModes(
                    updateNotificationRequest.isNotifyViaSMS(),
                    updateNotificationRequest.isNotifyViaEmail(),
                    id);
            log.info("Modes of Notification Updated!");

            sendNotification(
                    "This notification has been enabled for reservation " + id,
                    updateNotificationRequest.isNotifyViaSMS(),
                    updateNotificationRequest.isNotifyViaEmail());
        } else {
            log.error("Reservation Not Found!");
            throw new BadRequestException("Error with the specified id.");
        }
    }

    public void cancelReservation(Long id) throws ReservationException, BadRequestException {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if(reservationOptional.isPresent()) {
            reservationRepository.cancelReservation(id);

            sendNotification(
                    "Canceling Reservations for " + id,
                    reservationOptional.get().isNotifyViaSMS(),
                    reservationOptional.get().isNotifyViaEmail());
        } else {
            log.error("Reservation Not Found!");
            throw new BadRequestException("Error with the specified id.");
        }
    }

    public void sendReminder() {
        reservationRepository.findAll().stream()
                .filter(Reservation::isActive)
                .filter(r -> {

                    //Comment this out if you will test below
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(r.getReservationDateTime());
                    calendar.add(Calendar.HOUR, -4);
                    Date fourHoursPrior = calendar.getTime();
                    return r.getReservationDateTime().after(fourHoursPrior);

                    //Use this and comment out above to check if it notifies every 3 seconds
                    //return r.getReservationDateTime().after(new Date());
                })
                .forEach(r -> {
                    try {
                        sendNotification(
                                "Created " + getReservationMessage(
                                        r.getName(),
                                        r.getContactNumber(),
                                        r.getEmail(),
                                        r.getReservationDateTime(),
                                        r.getNumberOfGuests()),
                                r.isNotifyViaSMS(),
                                r.isNotifyViaEmail());
                        log.info("Reminder Notifications sent.");
                    } catch (ReservationException e) {
                        log.error("Error sending reservation notification.");
                    }
                });
    }

    private void sendNotification(String message, boolean shouldNotifySMS, boolean shouldNotifyEmail) throws ReservationException {
        try {
            sendMessageViaKafka(message, shouldNotifySMS, shouldNotifyEmail);
            log.info("Kafka used for notification");
        } catch (Exception ex) {
            sendMessage(message, shouldNotifySMS, shouldNotifyEmail);
            log.info("Same thread used for notification");
        }
    }

    private void sendMessage(String message, boolean shouldNotifySMS, boolean shouldNotifyEmail) {
        if(shouldNotifySMS) {
            smsService.notify(message);
        }
        if(shouldNotifyEmail) {
            emailService.notify(message);
        }
    }

    private void sendMessageViaKafka(String message, boolean shouldNotifySMS, boolean shouldNotifyEmail) throws ReservationException {
        if(shouldNotifySMS) {
            messageProducer.sendMessage(SMS_TOPIC, message);
        }
        if(shouldNotifyEmail) {
            messageProducer.sendMessage(EMAIL_TOPIC, message);
        }
    }

    private ReservationResponse reservationMapper(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .name(reservation.getName())
                .email(reservation.getEmail())
                .contactNumber(reservation.getContactNumber())
                .reservationDateTime(reservation.getReservationDateTime())
                .numberOfGuests(reservation.getNumberOfGuests())
                .isActive(reservation.isActive())
                .notifyViaEmail(reservation.isNotifyViaEmail())
                .notifyViaSMS(reservation.isNotifyViaSMS())
                .build();
    }

    private static String getReservationMessage(ReservationRequest reservationRequest) {
        return getReservationMessage(
                reservationRequest.getName(),
                reservationRequest.getContactNumber(),
                reservationRequest.getEmail(),
                reservationRequest.getReservationDateTime(),
                reservationRequest.getNumberOfGuests());
    }

    private static String getReservationMessage(String name, String contactNumber, String email, Date reservationDateTime, int numberOfGuests) {
        return "Reservation for - " + name + ", " +
                "with contact number - " + contactNumber + ", " +
                "with email - " + email + ", " +
                "with reservation date and time - " + reservationDateTime + ", " +
                "with number of guests - " + numberOfGuests + ", ";
    }
}
