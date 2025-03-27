package com.example.lucky7.domain.reservation.distributed.service;

public interface DistributedReservationLockManager {
    void executeWithLock(Long key, Runnable task) throws InterruptedException;
}
