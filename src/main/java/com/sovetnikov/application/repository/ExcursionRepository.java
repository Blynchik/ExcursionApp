package com.sovetnikov.application.repository;

import com.sovetnikov.application.aspect.LogExecutionTime;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExcursionRepository extends JpaRepository<Excursion, Integer> {

    List<Excursion> findByNameStartingWithIgnoreCase(String query);

    @LogExecutionTime
    @EntityGraph(attributePaths = {"users"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query(value = "SELECT r FROM Excursion r WHERE r.id=?1")
    Optional<Excursion> getWithUsers(int id);
}
