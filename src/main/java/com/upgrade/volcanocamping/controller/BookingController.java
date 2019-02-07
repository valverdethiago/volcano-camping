package com.upgrade.volcanocamping.controller;

import com.upgrade.volcanocamping.dto.BookingDto;
import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.User;
import com.upgrade.volcanocamping.service.BookingService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Set;

@RestController
@Api(basePath = "/api/booking", description = "Operations about bookings")
@RequestMapping(path = "/api/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(path = "/availableDates")
    @ApiOperation(value = "Finds available dates for booking",
            response = LocalDate.class,
            responseContainer = "List" )
    public ResponseEntity<Collection<LocalDate>> getAvailableDates(
            @ApiParam(name = "startDate", value = "Start date for the search", type = "String", format = "yyyy-MM-dd", required = false)
            @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(name = "endDate", value = "End date for the search", type = "String", format = "yyyy-MM-dd", required = false)
            @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return ResponseEntity.ok(this.bookingService.findAvailableDates(startDate, endDate));
    }

    @PostMapping
    @ApiOperation(value = "Record booking for the user in the selected date range")
    public ResponseEntity<Long> book(@RequestBody BookingDto bookingDto) {
        User user = new User();
        user.setEmail(bookingDto.getEmail());
        user.setFullName(bookingDto.getFullName());
        Booking booking = this.bookingService.book(user, bookingDto.getInitialDate(), bookingDto.getDepartureDate());
        return ResponseEntity.ok(booking.getId());
    }

    @DeleteMapping(path = "/{bookingId}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "Cancel booking with the given id")
    public void cancel(@PathVariable(name = "bookingId", required = true) Long bookingId) {
        this.bookingService.cancel(bookingId);
    }

    /**
     * Global Exception handler for all exceptions.
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handle(Exception exception) {
        AbstractMap.SimpleEntry<String, String> response =
                new AbstractMap.SimpleEntry<>("message", exception.getMessage());
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
