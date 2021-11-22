package com.epam.training.ticketservice.backend.room.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RoomDto {

    private final String name;
    private final Integer rowCount;
    private final Integer colCount;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "Room " + name + " with " + rowCount * colCount + " seats, "
                + rowCount + " rows and " + colCount + " columns";
    }

    public static class Builder {

        private String name;
        private Integer rowCount;
        private Integer colCount;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withRowCount(Integer rowCount) {
            this.rowCount = rowCount;
            return this;
        }

        public Builder withColCount(Integer colCount) {
            this.colCount = colCount;
            return this;
        }

        public RoomDto build() {
            return new RoomDto(name, rowCount, colCount);
        }
    }

}
