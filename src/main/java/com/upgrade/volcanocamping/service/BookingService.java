package com.upgrade.volcanocamping.service;

import java.time.LocalDate;
import java.util.Set;

public interface BookingService {

    Set<LocalDate> findAvailableDates(LocalDate initialDate, LocalDate endDate);
}
