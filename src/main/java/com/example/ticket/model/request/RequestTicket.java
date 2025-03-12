package com.example.ticket.model.request;

import lombok.Data;

import java.time.LocalDate;
@Data
public class RequestTicket {
    private String passengerName;
    private LocalDate travelDate;
    private String sourceStation;
    private String destinationStation;
    private double price;
    private boolean paymentStatus;
    private String ticketStatus;
    private int seatNumber;
}
