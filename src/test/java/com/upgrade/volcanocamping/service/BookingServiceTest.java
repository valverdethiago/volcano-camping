package com.upgrade.volcanocamping.service;

import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableList;
import com.upgrade.volcanocamping.exceptions.BookingAlreadyFinishedException;
import com.upgrade.volcanocamping.exceptions.InvalidDateIntervalException;
import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.User;
import com.upgrade.volcanocamping.repository.BookingRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static com.upgrade.volcanocamping.service.BookingServiceImpl.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BookingServiceTest {

    private BookingService bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private Faker faker = new Faker();

    @Before
    public void setup() {
        this.bookingService = new BookingServiceImpl(this.bookingRepository);
    }


    @Test
    public void givenOneBookingThenShouldReturnEmptyAvailableDates() {
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
    public void givenOneBookingOf2DaysThenShouldReturn28AvailableDates() {
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
    public void givenEmptyBookingListThenShouldReturn30AvailableDates() {
        // Arrange
        when(bookingRepository.findReservationsBetweenDates(any(), any()))
                .thenReturn(new ArrayList<>());
        // Act
        // at this case the service is expected to use the current date as initial and 30 days after as endDate
        Set<LocalDate> availableDates = this.bookingService.findAvailableDates(null, null);
        // Assert
        assertThat(availableDates, hasSize(30));
    }

    @Test
    public void givenNullStartDateThenShouldThrowException() {
        //Arrange
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(INVALID_BOOKING_DATES_EXCEPTION_MESSAGE);
        // Act
        bookingService.book(this.createFakeUser("fake-user@mail.com"),
                null, LocalDate.now().plusDays(10));
        //Assert is not necessary because we expect that an exception will be thrown
    }

    @Test
    public void givenNullEndDateThenShouldThrowException() {
        //Arrange
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(INVALID_BOOKING_DATES_EXCEPTION_MESSAGE);
        // Act
        bookingService.book(this.createFakeUser("fake-user@mail.com"),
                LocalDate.now().plusDays(10), null);
        //Assert is not necessary because we expect that an exception will be thrown
    }

    @Test
    public void givenEndDateBeforeStartDateThenShouldThrowException() {
        //Arrange
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(INVALID_BOOKING_DATES_EXCEPTION_MESSAGE);
        // Act
        bookingService.book(this.createFakeUser("fake-user@mail.com"),
                LocalDate.now().plusDays(20), LocalDate.now().plusDays(10));
        //Assert is not necessary because we expect that an exception will be thrown
    }

    @Test
    public void givenMaxLimitBookingReachedThenShouldThrowException() {
        //Arrange
        expectedException.expect(InvalidDateIntervalException.class);
        expectedException.expectMessage(MAX_DAYS_LIMIT_EXCEPTION_MESSAGE);
        // Act
        bookingService.book(this.createFakeUser("fake-user@mail.com"),
                LocalDate.now().plusDays(10), LocalDate.now().plusDays(20));
        //Assert is not necessary because we expect that an exception will be thrown
    }

    @Test
    public void givenTodayStartDateThenShouldThrowException() {
        //Arrange
        expectedException.expect(InvalidDateIntervalException.class);
        expectedException.expectMessage(INVALID_LIMITS);
        // Act
        bookingService.book(this.createFakeUser("fake-user@mail.com"),
                LocalDate.now(), LocalDate.now().plusDays(2));
        //Assert is not necessary because we expect that an exception will be thrown
    }

    @Test
    public void givenStartDateIs60DaysInAdvanceThenShouldThrowException() {
        //Arrange
        expectedException.expect(InvalidDateIntervalException.class);
        expectedException.expectMessage(INVALID_LIMITS);
        // Act
        bookingService.book(this.createFakeUser("fake-user@mail.com"),
                LocalDate.now().plusDays(60), LocalDate.now().plusDays(62));
        //Assert is not necessary because we expect that an exception will be thrown
    }

    @Test
    public void givenValidBookingThenShouldCallSaveMethod() {
        // Act
        bookingService.book(this.createFakeUser("fake-user@mail.com"),
                LocalDate.now().plusDays(10), LocalDate.now().plusDays(12));
        //Assert
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    public void givenEmptyBookingIdThenShouldThrowException() {
        //Arrange
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(BOOKING_ID_IS_MANDATORY_EXCEPTION_MESSAGE);
        // Act
        bookingService.cancel(null);
        //Assert is not necessary because we expect that an exception will be thrown
    }

    @Test
    public void givenInvalidBookingIdThenShouldThrowException() {
        //Arrange
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(INVALID_BOOKING_ID_EXCEPTION_MESSAGE);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        // Act
        bookingService.cancel(new Random().nextLong());
        //Assert is not necessary because we expect that an exception will be thrown
    }

    @Test(expected = BookingAlreadyFinishedException.class)
    public void givenAlreadyFinishedBookingThenShouldThrowException() {
        //Arrange
        Booking booking = createFakeBooking("fake-memeber@mail.com",
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(8));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        // Act
        bookingService.cancel(new Random().nextLong());
        //Assert is not necessary because we expect that an exception will be thrown
    }

    @Test
    public void givenValidBookingIdThenShouldCallMethod() {
        //Arrange
        Long expectedId = new Random().nextLong();
        Booking booking = createFakeBooking("fake-memeber@mail.com",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        booking.setId(expectedId);
        when(bookingRepository.findById(anyLong())).thenReturn(
                Optional.of(booking));
        // Act
        bookingService.cancel(expectedId);
        //Assert
        verify(bookingRepository, times(1)).save(booking);
    }

    private Booking createFakeBooking(String memberEmail,
                                      LocalDate initialDate,
                                      LocalDate departureDate) {
        User user = createFakeUser(memberEmail);
        Booking booking = Booking.builder()
                .user(user)
                .initialDate(initialDate)
                .departureDate(departureDate)
                .build();
        return booking;
    }

    private User createFakeUser(String memberEmail) {
        User user = User.builder()
                .email(memberEmail)
                .fullName(faker.name().fullName())
                .build();
        return user;
    }
}
