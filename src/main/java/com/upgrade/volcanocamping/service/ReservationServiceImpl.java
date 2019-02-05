package com.upgrade.volcanocamping.service;

import com.upgrade.volcanocamping.model.Reservation;
import com.upgrade.volcanocamping.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<LocalDate> findAvailableDates(LocalDate initialDate, LocalDate endDate) {
        if(initialDate == null) {
            initialDate = LocalDate.now();
        }
        if(endDate == null) {
            initialDate = initialDate.plusDays(30);
        }
        LocalDateTime initialDateTime = LocalDateTime.from(initialDate).with(LocalTime.of(12,0));
        LocalDateTime endDateTime = LocalDateTime.from(initialDate).with(LocalTime.of(12,0));
        List<Reservation> reservations = this.reservationRepository.findReservationsBetweenDates(initialDateTime, endDateTime);
        List<LocalDate> availableDates = new ArrayList<>();
        for (LocalDateTime date = initialDateTime; date.isBefore(endDateTime); date = date.plusDays(1)) {
            for(Reservation reservation : reservations) {
                if( !(date.isAfter(reservation.getInitialDate()) && date.isBefore(reservation.getDepartureDate())) ) {
                    availableDates.add(date.toLocalDate());
                }
            }
        }
        return availableDates;
    }
}
