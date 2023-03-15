package com.sovetnikov.application.repository;

import com.sovetnikov.application.aspect.LogExecutionTime;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.Like;
import com.sovetnikov.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    @LogExecutionTime
    @Query(value = "SELECT r FROM Like r WHERE r.user.id=?1")
    List<Like> getUserLikes(int userId);

    @LogExecutionTime
    @Query(value = "SELECT r FROM Like r WHERE r.excursion.id=?1")
    List<Like> getExcursionLikes(int excursionId);

    Optional<Like> findByExcursionAndUser(Excursion excursion, User user);

    void deleteAllByExcursion_Id(int excursionId);
}
