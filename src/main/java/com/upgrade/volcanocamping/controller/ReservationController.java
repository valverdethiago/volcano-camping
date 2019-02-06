package com.upgrade.volcanocamping.controller;

import com.upgrade.volcanocamping.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;

@RestController
public class ReservationController {

    private final BookingService bookingService;

    @Autowired
    public ReservationController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(path = "/api/booking/availableDates")
    public ResponseEntity<Collection<LocalDate>> getAvailableDates(LocalDate initialDate, LocalDate endDate) {
        return ResponseEntity.ok(this.bookingService.findAvailableDates(initialDate, endDate));
    }
}
