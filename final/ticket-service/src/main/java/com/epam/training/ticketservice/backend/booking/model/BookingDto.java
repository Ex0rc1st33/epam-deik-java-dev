package com.epam.training.ticketservice.backend.booking.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BookingDto {

    private final String movieTitle;
    private final String roomName;
    private final String startedAt;
    private final String seats;
    private final String user;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String movieTitle;
        private String roomName;
        private String startedAt;
        private String seats;
        private String user;

        public Builder withMovieTitle(String movieTitle) {
            this.movieTitle = movieTitle;
            return this;
        }

        public Builder withRoomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Builder withStartedAt(String startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public Builder withSeats(String seats) {
            this.seats = seats;
            return this;
        }

        public Builder withUser(String user) {
            this.user = user;
            return this;
        }

        public BookingDto build() {
            return new BookingDto(movieTitle, roomName, startedAt, seats, user);
        }

    }

}
