package com.upgrade.volcanocamping.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.time.LocalDate;

/**
 * Created by valve on 06/02/2019.
 */
@ApiModel(value="Booking", description = "Booking Request Object")
public class BookingDto {
    @ApiModelProperty(value="User email", dataType = "String", required = true)
    private String email;
    @ApiModelProperty(value="User full name", dataType = "String", required = true)
    private String fullName;
    @ApiModelProperty(value="Initial date for reservation", dataType = "String", required = true)
    private LocalDate initialDate;
    @ApiModelProperty(value="End date for reservation", dataType = "String", required = true)
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
