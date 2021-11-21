package com.epam.training.ticketservice.backend.screening.persistance.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String movieTitle;
    private String roomName;
    private String startedAt;

    public Screening(String movieTitle, String roomName, String startedAt) {
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.startedAt = startedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Screening screening = (Screening) o;
        return Objects.equals(id, screening.id) && Objects.equals(movieTitle, screening.movieTitle) && Objects.equals(roomName, screening.roomName) && Objects.equals(startedAt, screening.startedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movieTitle, roomName, startedAt);
    }

    @Override
    public String toString() {
        return "Screening{" + "id=" + id + ", movieTitle='" + movieTitle + '\'' + ", roomName='" + roomName + '\'' + ", startedAt=" + startedAt + '}';
    }

}
