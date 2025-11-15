package com.flightreservation.flightreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationUpdateRequest {

    private Long id;
    private boolean checkedIn;
    private int numberOfBags;
}
