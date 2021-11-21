package com.epam.training.ticketservice.backend.room.persistance.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    private String name;
    private Integer rowCount;
    private Integer colCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(name, room.name) && Objects.equals(rowCount, room.rowCount) && Objects.equals(colCount, room.colCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rowCount, colCount);
    }

    @Override
    public String toString() {
        return "Room{" + "name='" + name + '\'' + ", rowCount=" + rowCount + ", colCount=" + colCount + '}';
    }

}
