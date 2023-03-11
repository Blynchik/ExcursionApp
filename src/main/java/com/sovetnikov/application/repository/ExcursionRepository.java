package com.sovetnikov.application.repository;

import com.sovetnikov.application.model.Excursion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExcursionRepository extends JpaRepository<Excursion, Integer> {

    List<Excursion> findByNameStartingWithIgnoreCase(String query);
}
