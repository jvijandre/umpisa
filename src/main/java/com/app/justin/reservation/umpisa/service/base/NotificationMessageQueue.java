package com.app.justin.reservation.umpisa.service.base;

public interface NotificationMessageQueue {

    public void notify(String message) throws InterruptedException;
}
