/**
 *
 */
package com.upgrade.volcanocamping.repository;

import com.upgrade.volcanocamping.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for basic operations on Booking entity.
 * @author valverde.thiago
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(" select e from Booking e " +
            " where e.cancellationDate is null " +
            "   and ( e.initialDate between :initialDate and :endDate" +
            "         or e.departureDate between :initialDate and :endDate ) "  )
    List<Booking> findReservationsBetweenDates(@Param("initialDate") LocalDate initialDate,
                                               @Param("endDate") LocalDate endDate);
}
