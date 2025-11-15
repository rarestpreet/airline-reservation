package com.flightreservation.flightreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {

    private Long flightId;
    private String passengerFirstName;
    private String passengerMiddleName;
    private String passengerLastName;
    private String passengerEmail;
    private String passengerPhone;
    private String nameOnTheCard;
    private String cardNumber;
    private String expirationDate;
    private String securityCode;
}
