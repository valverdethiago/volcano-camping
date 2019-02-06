package com.upgrade.volcanocamping.service;

import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookingServiceImpl implements BookingService {

    public static final LocalTime DEFAULT_DEPARTURE_TIME = LocalTime.of(12, 0);
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Set<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate) {
        if(startDate == null) {
            startDate = LocalDate.now();
        }
        if(endDate == null) {
            endDate = startDate.plusDays(30);
        }
        List<Booking> bookings = this.bookingRepository.findReservationsBetweenDates(startDate, endDate);
        if(bookings.isEmpty()) {
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
            return Stream.iterate(startDate, date -> date.plusDays(1))
                    .limit(daysBetween)
                    .collect(Collectors.toSet());
        }
        Set<LocalDate> availableDates = new HashSet<>();
        dateLoop: for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            bookingLoop: for(Booking booking : bookings) {
                if( isDateWithinPeriod(date, booking.getInitialDate(), booking.getDepartureDate()) ) {
                    availableDates.remove(date);
                    continue dateLoop;
                }
                else
                    availableDates.add(date);
            }
        }
        return availableDates;
    }

    private boolean isDateWithinPeriod(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return ( date.equals(startDate) || date.isAfter(startDate) ) && date.isBefore(endDate);
    }

}
