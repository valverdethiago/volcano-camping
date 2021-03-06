package com.upgrade.volcanocamping.service;

import com.upgrade.volcanocamping.exceptions.BookingAlreadyFinishedException;
import com.upgrade.volcanocamping.exceptions.InvalidDateIntervalException;
import com.upgrade.volcanocamping.lock.BlockingResource;
import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.User;
import com.upgrade.volcanocamping.repository.BookingRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    public static final String INVALID_BOOKING_DATES_EXCEPTION_MESSAGE =
            "Invalid booking dates";
    public static final String MAX_DAYS_LIMIT_EXCEPTION_MESSAGE =
            "The campsite can be reserved for max 3 days";
    public static final String INVALID_LIMITS =
            "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance";
    public static final String BOOKING_ID_IS_MANDATORY_EXCEPTION_MESSAGE = "Booking ID is mandatory";
    public static final String INVALID_BOOKING_ID_EXCEPTION_MESSAGE = "Invalid Booking ID";
    public static final String RESOURCE_ID = "booking";

    @Autowired
    @Getter
    @Setter
    private BookingRepository bookingRepository;

    @Override
    public Set<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = startDate.plusDays(30);
        }
        if(startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(INVALID_BOOKING_DATES_EXCEPTION_MESSAGE);
        }
        List<Booking> bookings = this.bookingRepository.findReservationsBetweenDates(startDate, endDate);
        if (bookings.isEmpty()) {
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
            return Stream.iterate(startDate, date -> date.plusDays(1))
                    .limit(daysBetween)
                    .collect(Collectors.toSet());
        }
        Set<LocalDate> availableDates = new HashSet<>();
        dateLoop:
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            bookingLoop:
            for (Booking booking : bookings) {
                if (isDateWithinPeriod(date, booking.getInitialDate(), booking.getDepartureDate())) {
                    availableDates.remove(date);
                    continue dateLoop;
                }
                availableDates.add(date);
            }
        }
        return availableDates;
    }

    @Override
    @BlockingResource(resourceId = RESOURCE_ID)
    public Booking book(User user, LocalDate startDate, LocalDate endDate) {
        if (startDate == null
                || endDate == null
                || endDate.isBefore(startDate)
                || startDate.equals(endDate)) {
            throw new IllegalArgumentException(INVALID_BOOKING_DATES_EXCEPTION_MESSAGE);
        }
        long daysBetweenStartAndEnd = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetweenStartAndEnd > 3) {
            throw new InvalidDateIntervalException(MAX_DAYS_LIMIT_EXCEPTION_MESSAGE);
        }
        long daysBetweenNowAndStart = ChronoUnit.DAYS.between(LocalDate.now(), startDate);
        if (daysBetweenNowAndStart < 1 || daysBetweenNowAndStart > 30) {
            throw new InvalidDateIntervalException(INVALID_LIMITS);
        }
        return bookingRepository.save(
                Booking.builder().initialDate(startDate).departureDate(endDate).user(user).build()
        );
    }

    @Override
    public void cancel(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException(BOOKING_ID_IS_MANDATORY_EXCEPTION_MESSAGE);
        }
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        Booking elem = booking.orElseThrow(() -> new IllegalArgumentException(INVALID_BOOKING_ID_EXCEPTION_MESSAGE));
        if (elem.getDepartureDate().isBefore(LocalDate.now())) {
            throw new BookingAlreadyFinishedException();
        }
        elem.setCancellationDate(LocalDateTime.now());
        this.bookingRepository.save(elem);


    }

    private boolean isDateWithinPeriod(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return (date.equals(startDate) || date.isAfter(startDate)) && date.isBefore(endDate);
    }

}
