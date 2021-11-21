package com.epam.training.ticketservice.backend.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class MovieDTO {

    private final String title;
    private final String genre;
    private final Integer length;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDTO movieDTO = (MovieDTO) o;
        return Objects.equals(title, movieDTO.title) && Objects.equals(genre, movieDTO.genre) && Objects.equals(length, movieDTO.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genre, length);
    }

    @Override
    public String toString() {
        return title + " (" + genre + ", " + length + " minutes)";
    }

    public static class Builder {

        private String title;
        private String genre;
        private Integer length;

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder withLength(Integer length) {
            this.length = length;
            return this;
        }

        public MovieDTO build() {
            return new MovieDTO(title, genre, length);
        }
    }

}
