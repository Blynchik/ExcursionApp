package com.sovetnikov.application.repository;

import com.sovetnikov.application.aspect.LogExecutionTime;
import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {


    @LogExecutionTime
    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query(value = "SELECT r FROM Comment r WHERE r.id=?1")
    Optional<Comment> getWithUserAndExcursion(int id);

    @LogExecutionTime
    @Query(value = "SELECT r FROM Comment r WHERE r.user.id=?1")
    List<Comment> getUserComments(int userId);


    @LogExecutionTime
    @Query(value = "SELECT r FROM Comment r WHERE r.excursion.id=?1")
    List<Comment> getExcursionComments(int excursionId);
}
