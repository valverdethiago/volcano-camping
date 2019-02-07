package com.upgrade.volcanocamping.dto;

import javax.persistence.Column;
import java.time.LocalDate;

/**
 * Created by valve on 06/02/2019.
 */
public class BookingDto {
    private String email;
    private String fullName;
    private LocalDate initialDate;
    private LocalDate departureDate;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
}
