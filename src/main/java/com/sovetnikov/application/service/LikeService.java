package com.sovetnikov.application.service;

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

    @Transactional
    public void create(Like like){
            likeRepository.save(like);
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

    public Optional<Like> get(int id){
        return likeRepository.findById(id);
    }

    public Optional<Like> getByExcursionAndUser(Excursion excursion, User user){
        return likeRepository.findByExcursionAndUser(excursion,user);
    }
}
