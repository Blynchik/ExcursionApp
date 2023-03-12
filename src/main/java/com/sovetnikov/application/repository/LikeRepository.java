package com.sovetnikov.application.repository;

import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    @Query(value = "SELECT r FROM Like r WHERE r.user.id=?1")
    List<Like> getUserLikes(int userId);

    @Query(value = "SELECT r FROM Like r WHERE r.excursion.id=?1")
    List<Like> getExcursionLikes(int userId);
}
