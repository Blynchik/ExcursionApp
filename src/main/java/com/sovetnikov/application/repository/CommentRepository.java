package com.sovetnikov.application.repository;

import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.model.Excursion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query(value = "SELECT r FROM Comment r WHERE r.id=?1")
    Optional<Comment> getWithUserAndExcursion(int id);
}
