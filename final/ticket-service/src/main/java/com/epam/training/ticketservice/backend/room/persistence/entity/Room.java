package com.epam.training.ticketservice.backend.room.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    private String name;
    private Integer rowCount;
    private Integer colCount;
    private String priceComponent;

}
