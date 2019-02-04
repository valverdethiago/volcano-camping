/**
 *
 */
package com.upgrade.volcanocamping.repositories;

import com.upgrade.volcanocamping.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Member repository for basic operations on Reservation entity.
 * @author valverde.thiago
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
