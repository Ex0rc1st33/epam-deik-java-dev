package com.epam.training.ticketservice.backend.screening.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String movieTitle;
    private String roomName;
    private String startedAt;
    private String priceComponent = null;

    public Screening(String movieTitle, String roomName, String startedAt) {
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.startedAt = startedAt;
    }

}
