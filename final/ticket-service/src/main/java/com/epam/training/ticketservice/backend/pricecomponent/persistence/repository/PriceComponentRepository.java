package com.epam.training.ticketservice.backend.pricecomponent.persistence.repository;

import com.epam.training.ticketservice.backend.pricecomponent.persistence.entity.PriceComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceComponentRepository extends JpaRepository<PriceComponent, String> {

}
