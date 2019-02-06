package com.upgrade.volcanocamping.services;

import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableList;
import com.upgrade.volcanocamping.model.Reservation;
import com.upgrade.volcanocamping.model.User;
import com.upgrade.volcanocamping.repositories.ReservationRepository;
import com.upgrade.volcanocamping.service.ReservationService;
import com.upgrade.volcanocamping.service.ReservationServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void givenOneReservationsThenReturnEmptyAvailableDates() {
        // Arrange
        Reservation reservation = this.createFakeReservation("fake-user@mail.com",
                LocalDateTime.now().minusDays(12).toLocalDate(),
                LocalDateTime.now().minusDays(10).toLocalDate());
        // Act
        Set<LocalDate> availableDates = this.reservationService.findAvailableDates(
                reservation.getInitialDate(),
                reservation.getDepartureDate());
        // Assert
        assertThat(availableDates, is(empty()));
    }

    @Test
    public void givenTwoReservationsThenReturnAvailableDates() {
        // Arrange
        this.createFakeReservation("fake-user@mail.com",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12));
        this.createFakeReservation("fake-user@mail.com",
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(4));
        int monthMaxDays = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        // this two reservations has 4 days
        int expectedDatesToReturn = monthMaxDays -4;
        // Act
        // at this case the service is expected to use the current date as initial and 30 days after as endDate
        Set<LocalDate> availableDates = this.reservationService.findAvailableDates(null, null);
        // Assert
        assertThat(availableDates, hasSize(expectedDatesToReturn));
    }

    private Reservation createFakeReservation(String memberEmail,
                                                 LocalDate initialDate,
                                                 LocalDate departureDate) {
        Faker faker = new Faker();
        User user = new User();
        user.setEmail(memberEmail);
        user.setFullName(faker.name().fullName());
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setInitialDate(initialDate);
        reservation.setDepartureDate(departureDate);
        return this.reservationRepository.save(reservation);
    }
}
