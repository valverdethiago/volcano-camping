package com.upgrade.volcanocamping.services;

import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableList;
import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.User;
import com.upgrade.volcanocamping.repositories.BookingRepository;
import com.upgrade.volcanocamping.service.BookingService;
import com.upgrade.volcanocamping.service.BookingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BookingServiceTest {

    private BookingService bookingService;
    @Mock
    private BookingRepository bookingRepository;

    @Before
    public void setup() {
        this.bookingService = new BookingServiceImpl(this.bookingRepository);
    }


    @Test
    public void givenOneBookingThenReturnEmptyAvailableDates() {
        // Arrange
        Booking booking = this.createFakeBooking("fake-user@mail.com",
                LocalDateTime.now().minusDays(12).toLocalDate(),
                LocalDateTime.now().minusDays(10).toLocalDate());
        when(bookingRepository.findReservationsBetweenDates(any(), any()))
                .thenReturn(ImmutableList.of(booking));
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
        Booking booking = this.createFakeBooking("fake-user@mail.com",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12));
        when(bookingRepository.findReservationsBetweenDates(any(), any()))
                .thenReturn(ImmutableList.of(booking));
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
        when(bookingRepository.findReservationsBetweenDates(any(), any()))
                .thenReturn(new ArrayList<>());
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
        return booking;
    }
}
