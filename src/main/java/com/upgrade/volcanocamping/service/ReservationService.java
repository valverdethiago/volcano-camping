package com.upgrade.volcanocamping.service;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    List<LocalDate> findAvailableDates(LocalDate initialDate, LocalDate endDate);
}
