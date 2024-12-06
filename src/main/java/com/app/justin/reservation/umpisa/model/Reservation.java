package com.app.justin.reservation.umpisa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "t_reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String contactNumber;
    private String email;
    private Date reservationDateTime;
    private int numberOfGuests;
    private boolean isActive;
    private boolean notifyViaSMS;
    private boolean notifyViaEmail;
}
