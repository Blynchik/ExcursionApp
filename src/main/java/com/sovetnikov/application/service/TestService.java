package com.sovetnikov.application.service;

import com.sovetnikov.application.aspect.LogExecutionTime;
import com.sovetnikov.application.repository.CommentRepository;
import com.sovetnikov.application.repository.ExcursionRepository;
import com.sovetnikov.application.repository.LikeRepository;
import com.sovetnikov.application.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TestService {
    private final UserRepository userRepository;
    private final ExcursionRepository excursionRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public TestService(UserRepository userRepository,
                       ExcursionRepository excursionRepository,
                       CommentRepository commentRepository,
                       LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.excursionRepository = excursionRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }

    @LogExecutionTime
    public void test(){
        System.out.println("CHECK N+1:");

        System.out.println("USER AND HIS EXCURSIONS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(userRepository.getReferenceById(1).getExcursions());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(userRepository.getWithExcursions(1).get().getExcursions());
        System.out.println("========================\n========================");

        System.out.println("USER AND HIS LIKES:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(userRepository.getReferenceById(1).getLikes());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(likeRepository.getUserLikes(1));
        System.out.println("========================\n========================");

        System.out.println("USER AND HIS COMMENTS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(userRepository.getReferenceById(1).getComments());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(commentRepository.getUserComments(1));
        System.out.println("========================\n========================");

        System.out.println("EXCURSION AND ITS USERS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(excursionRepository.getReferenceById(1).getUsers());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(excursionRepository.getWithUsers(1));
        System.out.println("========================\n========================");

        System.out.println("EXCURSION AND ITS LIKES:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(excursionRepository.getReferenceById(1).getLikes());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(likeRepository.getExcursionLikes(1));
        System.out.println("========================\n========================");

        System.out.println("EXCURSION AND ITS COMMENTS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(excursionRepository.getReferenceById(1).getComments());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(commentRepository.getExcursionComments(1));
        System.out.println("========================\n========================");
    }
}
