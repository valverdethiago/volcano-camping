package com.upgrade.volcanocamping.controller;

import com.github.javafaker.Faker;
import com.upgrade.volcanocamping.TestUtil;
import com.upgrade.volcanocamping.dto.BookingDto;
import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.User;
import com.upgrade.volcanocamping.service.BookingService;
import org.apache.tomcat.jni.Local;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.upgrade.volcanocamping.service.BookingServiceImpl.*;

/**
 * Created by valve on 06/02/2019.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class BookingControllerTest {

    public static final int AVAILABLE_DATES_LENGTH = 20;
    @InjectMocks
    private BookingController bookingController;
    @Mock
    private BookingService bookingService;
    private MockMvc mockMvc;
    private Faker faker = new Faker();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .build();
    }

    @Test
    public void givenCompleteAvailableDateListReturnAllElements() throws Exception {
        //Arrange
        LocalDate startDate = LocalDate.now().plusDays(10);
        Set<LocalDate> availableDates = Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(AVAILABLE_DATES_LENGTH)
                .collect(Collectors.toSet());
        when(bookingService.findAvailableDates(any(), any())).thenReturn(availableDates);
        //Act
        mockMvc.perform(get("/api/booking/availableDates")
                .param("startDate", "2019-01-01")
                .param("endDate", "2019-01-31"))
                //Assert
                .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(AVAILABLE_DATES_LENGTH)));
    }

    @Test
    public void givenEmptyAvailableDatesReturnEmptyList() throws Exception {
        //Arrange
        when(bookingService.findAvailableDates(any(), any())).thenReturn(new HashSet<>());
        //Act
        mockMvc.perform(get("/api/booking/availableDates")
                .param("startDate", "2019-01-01")
                .param("endDate", "2019-01-31"))
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(empty())));
    }

    @Test
    public void givenEmptyStartDateShouldReturnErrorCodeWhenBooking() throws Exception {
        //Arrange
        BookingDto bookingDto = BookingDto.builder()
                .email("fake-user@mail.com")
                .fullName(faker.name().fullName())
                .initialDate(LocalDate.now().plusDays(30))
                .build();
        when(bookingService.book(any(), any(), any())).thenThrow(
                new IllegalArgumentException(INVALID_BOOKING_DATES_EXCEPTION_MESSAGE));
        //Act
        mockMvc.perform(post("/api/booking")
                .content(TestUtil.convertObjectToJsonBytes(bookingDto))
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                //Assert
                .andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    public void givenEmptyEndDateShouldReturnErrorCodeWhenBooking() throws Exception {
        //Arrange
        BookingDto bookingDto = BookingDto.builder()
                .email("fake-user@mail.com")
                .fullName(faker.name().fullName())
                .initialDate(LocalDate.now().plusDays(30))
                .build();
        when(bookingService.book(any(), any(), any())).thenThrow(
                new IllegalArgumentException(INVALID_BOOKING_DATES_EXCEPTION_MESSAGE));
        //Act
        mockMvc.perform(post("/api/booking")
                .content(TestUtil.convertObjectToJsonBytes(bookingDto))
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                //Assert
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void givenValidInputShouldReturnBookingWhenBooking() throws Exception {
        //Arrange
        BookingDto bookingDto = BookingDto.builder()
                .email("fake-user@mail.com")
                .fullName(faker.name().fullName())
                .initialDate(LocalDate.now().plusDays(1))
                .departureDate(LocalDate.now().plusDays(3))
                .build();
        User user = User.builder().email(bookingDto.getEmail())
                .fullName(bookingDto.getFullName())
                .build();
        Booking booking = Booking.builder().id(new Random().nextLong())
                .initialDate(bookingDto.getInitialDate())
                .departureDate(bookingDto.getDepartureDate())
                .user(user)
                .build();
        when(bookingService.book(any(), any(), any())).thenReturn(booking);
        //Act
        mockMvc.perform(post("/api/booking")
                .content(TestUtil.convertObjectToJsonBytes(bookingDto))
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(empty()))))
                .andExpect(jsonPath("$", is(booking.getId())));
    }

    @Test
    public void givenEmptyIdShouldReturnErrorCodeWhenCanceling() throws Exception {
        //Act
        mockMvc.perform(delete("/api/booking"))
                //Assert
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenInvalidIdShouldReturnErrorCodeWhenCancelling() throws Exception {
        //Arrange
        doThrow(new IllegalArgumentException(BOOKING_ID_IS_MANDATORY_EXCEPTION_MESSAGE))
                .when(bookingService).cancel(anyLong());
        //Act
        mockMvc.perform(delete(String.format("/api/booking/%d", new Random().nextLong())))
                //Assert
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenValidIdShouldReturnOkWhenCancelling() throws Exception {
        //Arrange
        doNothing().when(bookingService).cancel(anyLong());
        //Act
        mockMvc.perform(delete(String.format("/api/booking/%d", new Random().nextLong())))
                //Assert
                .andExpect(status().isOk());
    }

}
