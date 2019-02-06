package com.upgrade.volcanocamping.services;

import com.github.javafaker.Faker;
import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.User;
import com.upgrade.volcanocamping.repositories.BookingRepository;
import com.upgrade.volcanocamping.service.BookingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Set;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void givenOneReservationsThenReturnEmptyAvailableDates() {
        // Arrange
        Booking booking = this.createFakeBooking("fake-user@mail.com",
                LocalDateTime.now().minusDays(12).toLocalDate(),
                LocalDateTime.now().minusDays(10).toLocalDate());
        // Act
        Set<LocalDate> availableDates = this.bookingService.findAvailableDates(
                booking.getInitialDate(),
                booking.getDepartureDate());
        // Assert
        assertThat(availableDates, is(empty()));
    }

    @Test
    public void givenOneBookingOf2DaysThenReturn28AvailableDates() {
        // Arrange
        this.createFakeBooking("fake-user@mail.com",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12));
        // this reservations has 2 days and the search period would be 30 days
        int expectedDatesToReturn = 30-2;
        // Act
        // at this case the service is expected to use the current date as initial and 30 days after as endDate
        Set<LocalDate> availableDates = this.bookingService.findAvailableDates(null, null);
        // Assert
        assertThat(availableDates, hasSize(expectedDatesToReturn));
    }

    @Test
    public void givenEmptyBookingListThenReturn30AvailableDates() {
        // Arrange
        this.bookingRepository.deleteAll();
        // Act
        // at this case the service is expected to use the current date as initial and 30 days after as endDate
        Set<LocalDate> availableDates = this.bookingService.findAvailableDates(null, null);
        // Assert
        assertThat(availableDates, hasSize(30));
    }

    private Booking createFakeBooking(String memberEmail,
                                      LocalDate initialDate,
                                      LocalDate departureDate) {
        Faker faker = new Faker();
        User user = new User();
        user.setEmail(memberEmail);
        user.setFullName(faker.name().fullName());
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setInitialDate(initialDate);
        booking.setDepartureDate(departureDate);
        return this.bookingRepository.save(booking);
    }
}
