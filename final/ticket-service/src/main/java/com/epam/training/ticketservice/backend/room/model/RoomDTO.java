package com.epam.training.ticketservice.backend.room.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class RoomDTO {

    private final String name;
    private final Integer rowCount;
    private final Integer colCount;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDTO roomDTO = (RoomDTO) o;
        return Objects.equals(name, roomDTO.name) && Objects.equals(rowCount, roomDTO.rowCount) && Objects.equals(colCount, roomDTO.colCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rowCount, colCount);
    }

    @Override
    public String toString() {
        return "Room " + name + " with " + rowCount * colCount + " seats, " + rowCount + " rows and " + colCount + " columns";
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

        public RoomDTO build() {
            return new RoomDTO(name, rowCount, colCount);
        }
    }

}
