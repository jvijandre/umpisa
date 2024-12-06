package com.app.justin.reservation.umpisa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {

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
