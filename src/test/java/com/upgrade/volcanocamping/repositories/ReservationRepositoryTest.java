package com.upgrade.volcanocamping.repositories;

import com.github.javafaker.Faker;
import com.upgrade.volcanocamping.model.Reservation;
import com.upgrade.volcanocamping.model.User;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ReservationRepositoryTest {
	
	@Autowired
	private ReservationRepository reservationRepository;
	private Faker faker = new Faker();
	
	@Test
	public void shouldInsertSimpleReservation() {
		// Arrange
		Reservation reservation = this.createAndSaveReservation("fake-member@gmail.com",
				LocalDateTime.now(), LocalDateTime.now());
		// Act
		List<Reservation> reservationList = Lists.newArrayList(this.reservationRepository.findAll());
		// Assert
		assertNotNull(reservation.getId());
		assertThat(reservationList, containsInAnyOrder(reservation));
	}

	@Test
	public void shouldReturnSingleReservationInGivenPeriod() {
		// Arrange
		Reservation reservation = this.createAndSaveReservation("fake-member@gmail.com",
				LocalDateTime.of(2019, Month.JANUARY, 1, 12, 00, 00),
				LocalDateTime.of(2019, Month.JANUARY, 3, 12, 00, 00));
		Reservation reservation2 = this.createAndSaveReservation("fake-member2@gmail.com",
				LocalDateTime.of(2019, Month.FEBRUARY, 1, 12, 00, 00),
				LocalDateTime.of(2019, Month.FEBRUARY, 3, 12, 00, 00));
		// Act
		List<Reservation> reservationList = this.reservationRepository.findReservationsBetweenDates(
				LocalDateTime.of(2019, Month.JANUARY, 1, 0, 0, 0),
				LocalDateTime.of(2019, Month.JANUARY, 31, 23, 59, 59)
		);

		// Assert
		assertThat(reservationList, not(empty()));
		assertThat(reservationList, hasSize(1) );
		assertThat(reservationList, hasItem(reservation));
		assertThat(reservationList, not(hasItem(reservation2)));

	}


	private Reservation createAndSaveReservation(String memberEmail,
												LocalDateTime initialDate,
												LocalDateTime departureDate) {
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
