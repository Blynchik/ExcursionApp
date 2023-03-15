package com.sovetnikov.application.service;

import com.sovetnikov.application.aspect.LogExecutionTime;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.Like;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @LogExecutionTime
    @Transactional
    public void create(Like like) {
        likeRepository.save(like);
    }

    @LogExecutionTime
    public List<Like> getUserLikes(int userId) {
        return likeRepository.getUserLikes(userId);
    }

    @LogExecutionTime
    public List<Like> getExcursionLikes(int excursionId) {
        return likeRepository.getExcursionLikes(excursionId);
    }

    @LogExecutionTime
    @Transactional
    public void delete(int id) {
        likeRepository.deleteById(id);
    }

    @LogExecutionTime
    public Optional<Like> get(int id) {
        return likeRepository.findById(id);
    }

    @LogExecutionTime
    public Optional<Like> getByExcursionAndUser(Excursion excursion, User user) {
        return likeRepository.findByExcursionAndUser(excursion, user);
    }

    @LogExecutionTime
    @Transactional
    public void clearAllLikes(int excursionId) {
        likeRepository.deleteAllByExcursion_Id(excursionId);
    }
}
