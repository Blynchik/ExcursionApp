package com.sovetnikov.application.service;

import com.sovetnikov.application.model.Like;
import com.sovetnikov.application.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Transactional
    public void create(Like like) {
        if (!likeRepository.getUserLikes(like.getId()).contains(like)) {
            likeRepository.save(like);
        }
    }

    public List<Like> getUserLikes (int userId){
        return likeRepository.getUserLikes(userId);
    }

    public List<Like> getExcursionLikes(int excursionId){
        return likeRepository.getExcursionLikes(excursionId);
    }

    @Transactional
    public void delete(int id){
        likeRepository.deleteById(id);
    }
}
