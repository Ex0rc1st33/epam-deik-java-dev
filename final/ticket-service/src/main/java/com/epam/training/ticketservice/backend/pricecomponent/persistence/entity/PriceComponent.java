package com.epam.training.ticketservice.backend.pricecomponent.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceComponent {

    @Id
    private String name;
    private Integer value;

}
