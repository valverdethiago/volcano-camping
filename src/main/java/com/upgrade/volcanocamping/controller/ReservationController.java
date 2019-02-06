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

    @Autowired
    private BookingService bookingService;

    @GetMapping(path = "/api/reservation")
    public ResponseEntity<Collection<LocalDate>> getAvailableDates(LocalDate initialDate, LocalDate endDate) {
        return ResponseEntity.ok(this.bookingService.findAvailableDates(initialDate, endDate));
    }
}
