package com.upgrade.volcanocamping.repository;

import com.github.javafaker.Faker;
import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.User;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class BookingRepositoryTest {
	
	@Autowired
	private BookingRepository bookingRepository;
	private Faker faker = new Faker();
	
	@Test
	public void shouldInsertSimpleReservation() {
		// Arrange
		Booking booking = this.createAndSaveReservation("fake-member@gmail.com",
				LocalDate.now(), LocalDate.now().plusDays(1));
		// Act
		List<Booking> bookingList = Lists.newArrayList(this.bookingRepository.findAll());
		// Assert
		assertNotNull(booking.getId());
		assertThat(bookingList, containsInAnyOrder(booking));
	}

	@Test
	public void shouldReturnSingleReservationInGivenPeriod() {
		// Arrange
		Booking booking = this.createAndSaveReservation("fake-member@gmail.com",
				LocalDate.of(2019, Month.JANUARY, 1),
				LocalDate.of(2019, Month.JANUARY, 3));
		Booking booking2 = this.createAndSaveReservation("fake-member2@gmail.com",
				LocalDate.of(2019, Month.FEBRUARY, 1),
				LocalDate.of(2019, Month.FEBRUARY, 3));
		// Act
		List<Booking> bookingList = this.bookingRepository.findReservationsBetweenDates(
				LocalDate.of(2019, Month.JANUARY, 1),
				LocalDate.of(2019, Month.JANUARY, 31)
		);

		// Assert
		assertThat(bookingList, not(empty()));
		assertThat(bookingList, hasSize(1) );
		assertThat(bookingList, hasItem(booking));
		assertThat(bookingList, not(hasItem(booking2)));

	}


	private Booking createAndSaveReservation(String memberEmail,
											 LocalDate initialDate,
											 LocalDate departureDate) {
		User user = User.builder()
				.email(memberEmail)
				.fullName(faker.name().fullName())
				.build();
		Booking booking = Booking.builder()
				.user(user)
				.initialDate(initialDate)
				.departureDate(departureDate)
				.build();
		return this.bookingRepository.save(booking);
	}

}
