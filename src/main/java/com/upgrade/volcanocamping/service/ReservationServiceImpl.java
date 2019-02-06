package com.upgrade.volcanocamping.service;

import com.upgrade.volcanocamping.model.Reservation;
import com.upgrade.volcanocamping.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReservationServiceImpl implements ReservationService {

    public static final LocalTime DEFAULT_DEPARTURE_TIME = LocalTime.of(12, 0);
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Set<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate) {
        if(startDate == null) {
            startDate = LocalDate.now();
        }
        if(endDate == null) {
            endDate = startDate.plusDays(30);
        }
        List<Reservation> reservations = this.reservationRepository.findReservationsBetweenDates(startDate, endDate);
        if(reservations.isEmpty()) {
            long daysBetween = Duration.between(startDate, endDate).toDays();
            return Stream.iterate(startDate, date -> date.plusDays(1))
                    .limit(daysBetween)
                    .collect(Collectors.toSet());
        }
        Set<LocalDate> availableDates = new HashSet<>();
        dateLoop: for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            bookingLoop: for(Reservation reservation : reservations) {
                if( isDateWithinPeriod(date, reservation.getInitialDate(), reservation.getDepartureDate()) )
                    continue dateLoop;
                else
                    availableDates.add(date);
            }
        }
        return availableDates;
    }

    private boolean isDateWithinPeriod(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return ( date.equals(startDate) || date.isAfter(startDate) ) &&
                ( date.equals(endDate) || date.isBefore(endDate) );
    }

}
