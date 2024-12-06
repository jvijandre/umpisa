package com.app.justin.reservation.umpisa.repository;

import com.app.justin.reservation.umpisa.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.name = :name AND r.isActive = true")
    List<Reservation> getExistingReservations(@Param("name") String name);

    @Modifying
    @Query("UPDATE Reservation r SET r.isActive = false WHERE r.id = :id")
    void cancelReservation(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Reservation r SET r.reservationDateTime = :date, r.numberOfGuests = :totalGuests  WHERE r.id = :id")
    void updateTimeAndGuests(@Param("date") Date date, @Param("totalGuests") int totalGuests, @Param("id") Long id);

    @Modifying
    @Query("UPDATE Reservation r SET r.notifyViaSMS = :sms, r.notifyViaEmail = :email  WHERE r.id = :id")
    void updateNotificationModes(@Param("sms") boolean sms, @Param("email") boolean email, @Param("id") Long id);
}
