package com.app.justin.reservation.umpisa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Contact Number cannot be blank")
    private String contactNumber;

    @Email
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotNull(message = "Reservation Date-Time cannot be blank")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date reservationDateTime;

    @NotNull(message = "Number of Guests cannot be blank")
    private Integer numberOfGuests;

    private boolean isActive;
}
