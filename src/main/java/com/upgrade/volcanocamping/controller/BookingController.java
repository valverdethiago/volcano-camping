package com.upgrade.volcanocamping.controller;

import com.upgrade.volcanocamping.dto.BookingDto;
import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.User;
import com.upgrade.volcanocamping.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.Collection;

@RestController
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(path = "/api/booking/availableDates")
    public ResponseEntity<Collection<LocalDate>> getAvailableDates(
            @RequestParam(value="startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value="endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return ResponseEntity.ok(this.bookingService.findAvailableDates(startDate, endDate));
    }

    @PostMapping(path = "/api/booking")
    public ResponseEntity<Long> book(@RequestBody BookingDto bookingDto) {
        User user = new User();
        user.setEmail(bookingDto.getEmail());
        user.setFullName(bookingDto.getFullName());
        Booking booking = this.bookingService.book(user, bookingDto.getInitialDate(), bookingDto.getDepartureDate());
        return ResponseEntity.ok(booking.getId());
    }

    @DeleteMapping(path = "/api/booking/{bookingId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void cancel(@PathParam("bookingId") Long bookingId) {
        this.bookingService.cancel(bookingId);
    }

}
