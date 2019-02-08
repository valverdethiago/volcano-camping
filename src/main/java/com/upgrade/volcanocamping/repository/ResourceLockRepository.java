/**
 *
 */
package com.upgrade.volcanocamping.repository;

import com.upgrade.volcanocamping.model.Booking;
import com.upgrade.volcanocamping.model.ResourceLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for basic operations on resource locks.
 * @author valverde.thiago
 */
@Repository
public interface ResourceLockRepository extends JpaRepository<ResourceLock, Long> {

    @Query(" select e " +
            "  from ResourceLock e " +
            " where e.resourceId = :resourceId " +
            "   and (e.releaseTime is null or e.releaseTime <= current_date())")
    Optional<ResourceLock> findByResourceId(@Param("resourceId") String resourceId);
}
