/**
 *
 */
package com.upgrade.volcanocamping.repositories;

import com.upgrade.volcanocamping.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for basic operations on Reservation entity.
 * @author valverde.thiago
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(" select e from Reservation e " +
            " where e.initialDate between :initialDate and :endDate" +
            "    or e.departureDate between :initialDate and :endDate " )
    List<Reservation> findReservationsBetweenDates(@Param("initialDate") LocalDateTime initialDate,
                                                   @Param("endDate") LocalDateTime endDate);
}
