package com.epam.training.ticketservice.backend.room.persistence.entity;

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
public class Room {

    @Id
    private String name;
    private Integer rowCount;
    private Integer colCount;
    private String priceComponent = null;

    public Room(String name, Integer rowCount, Integer colCount) {
        this.name = name;
        this.rowCount = rowCount;
        this.colCount = colCount;
    }

}
