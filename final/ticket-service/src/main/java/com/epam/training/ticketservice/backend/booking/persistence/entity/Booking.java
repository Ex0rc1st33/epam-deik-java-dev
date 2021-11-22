package com.epam.training.ticketservice.backend.booking.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String movieTitle;
    private String roomName;
    private String startedAt;
    private String seats;
    private String user;
    private Integer basePrice;

    public Booking(String movieTitle, String roomName, String startedAt, String seats, String user, Integer basePrice) {
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.startedAt = startedAt;
        this.seats = seats;
        this.user = user;
        this.basePrice = basePrice;
    }

}
