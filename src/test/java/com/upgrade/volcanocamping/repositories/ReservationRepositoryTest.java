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
import java.util.List;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ReservationRepositoryTest {
	
	@Autowired
	private ReservationRepository reservationRepository;
	Faker faker = new Faker();
	
	@Test
	public void shouldInsertSimpleReservation() {
		// Arrange
		User user = new User();
		user.setEmail("fake-member@gmail.com");
		user.setFullName(faker.name().fullName());
		Reservation reservation = new Reservation();
		reservation.setUser(user);
		reservation.setInitialDate(LocalDateTime.now());
		reservation.setDepartureDate(LocalDateTime.now());
		// Act
		Reservation dbReservation = this.reservationRepository.save(reservation);
		List<Reservation> reservationList = Lists.newArrayList(this.reservationRepository.findAll());
		// Assert
		assertNotNull(dbReservation.getId());
		assertThat(reservationList, containsInAnyOrder(reservation));
	}

}
