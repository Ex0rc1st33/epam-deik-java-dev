package com.epam.training.ticketservice.backend.movie.persistence.repository;

import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

}
