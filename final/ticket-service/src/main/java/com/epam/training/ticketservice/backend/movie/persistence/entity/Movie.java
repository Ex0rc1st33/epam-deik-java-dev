package com.epam.training.ticketservice.backend.movie.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Movie {

    @Id
    private String title;
    private String genre;
    private Integer length;
    private String priceComponent = null;

    public Movie(String title, String genre, Integer length) {
        this.title = title;
        this.genre = genre;
        this.length = length;
    }

}
