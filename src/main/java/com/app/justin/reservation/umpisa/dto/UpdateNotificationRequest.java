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
public class UpdateNotificationRequest {

    private Long id;
    private boolean notifyViaSMS;
    private boolean notifyViaEmail;
}
