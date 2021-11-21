package com.epam.training.ticketservice.backend.user.persistance.repository;

import com.epam.training.ticketservice.backend.user.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsernameAndPasswordAndRole(String username, String password, User.Role role);

}
