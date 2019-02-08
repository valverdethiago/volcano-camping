package com.upgrade.volcanocamping.service;

import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableList;
import com.upgrade.volcanocamping.exceptions.MaxRetryLimitExceededException;
import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.ResourceLock;
import com.upgrade.volcanocamping.model.User;
import com.upgrade.volcanocamping.repository.BookingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownServiceException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static com.upgrade.volcanocamping.service.BookingServiceImpl.RESOURCE_ID;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class BookingServiceIntegrationTest {

    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private ResourceLockService resourceLockService;

    private Faker faker = new Faker();


    @Test(expected = MaxRetryLimitExceededException.class)
    public void tryToBookSameDateInParallel() throws Exception {
        //Arrange
        // locking the resource previously
        this.resourceLockService.acquire(RESOURCE_ID);
        //Act
        this.bookingService.book(User.builder()
                        .fullName(faker.name().fullName())
                        .email("fake-user@mail.com")
                        .build(),
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(6));
        //Assert is not needed cause we expect an exception to be thrown.
    }
}
