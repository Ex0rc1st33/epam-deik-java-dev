package com.epam.training.ticketservice.backend.movie.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class MovieDto {

    private final String title;
    private final String genre;
    private final Integer length;

    public static Builder builder() {
        return new Builder();
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

        public MovieDto build() {
            return new MovieDto(title, genre, length);
        }
    }

}
