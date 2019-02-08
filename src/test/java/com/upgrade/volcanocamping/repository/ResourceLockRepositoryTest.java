package com.upgrade.volcanocamping.repository;

import com.github.javafaker.Faker;
import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.ResourceLock;
import com.upgrade.volcanocamping.model.User;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ResourceLockRepositoryTest {
	
	@Autowired
	private ResourceLockRepository resourceLockRepository;
	
	@Test
	public void shouldInsertSimpleLock() {
		// Arrange
		ResourceLock lock = this.createAndSaveLock("booking");
		// Act
		List<ResourceLock> lockList = Lists.newArrayList(this.resourceLockRepository.findAll());
		// Assert
		assertNotNull(lock.getId());
		assertThat(lockList, containsInAnyOrder(lock));
	}

	private ResourceLock createAndSaveLock(String resourceId) {
		return this.resourceLockRepository.save(ResourceLock.builder()
				.lockingTime(LocalDateTime.now())
				.resourceId(resourceId)
				.build()
		);
	}

}
