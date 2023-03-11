package com.sovetnikov.application.repository;

import com.sovetnikov.application.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByNameStartingWithIgnoreCase(String query);

    @EntityGraph(attributePaths = {"excursions"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query(value = "SELECT r FROM User r WHERE r.id=?1")
    Optional<User> getWithExcursions(int id);
}
