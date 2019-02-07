package com.upgrade.volcanocamping.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by valve on 03/02/2019.
 */
@Entity
@Table(name = "booking")
@Data
@Builder
@EqualsAndHashCode(of={"id"})
@NoArgsConstructor
@AllArgsConstructor
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private User user;
    @Column(name="initial_date")
    private LocalDate initialDate;
    @Column(name="departure_date")
    private LocalDate departureDate;
    @Column(name="cancellation_date")
    private LocalDateTime cancellationDate;

}
