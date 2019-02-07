package com.upgrade.volcanocamping.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

/**
 * Created by valve on 06/02/2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
