package com.upgrade.volcanocamping.service;

import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.User;

import java.time.LocalDate;
import java.util.Set;

public interface BookingService {

    Set<LocalDate> findAvailableDates(LocalDate initialDate, LocalDate endDate);

    Booking book(User user, LocalDate startDate, LocalDate endDate);

    void cancel(Long bookingId);
}
