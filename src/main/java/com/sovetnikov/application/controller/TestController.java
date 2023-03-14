package com.sovetnikov.application.controller;

import com.sovetnikov.application.repository.CommentRepository;
import com.sovetnikov.application.repository.ExcursionRepository;
import com.sovetnikov.application.repository.LikeRepository;
import com.sovetnikov.application.repository.UserRepository;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.LikeService;
import com.sovetnikov.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/test")
public class TestController {

    private final UserRepository userRepository;
    private final ExcursionRepository excursionRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final ExcursionService excursionService;
    private final CommentService commentService;
    private final LikeService likeService;

    @Autowired
    public TestController(UserRepository userRepository,
                          ExcursionRepository excursionRepository,
                          CommentRepository commentRepository,
                          LikeRepository likeRepository,
                          UserService userService,
                          ExcursionService excursionService,
                          CommentService commentService,
                          LikeService likeService) {
        this.userRepository = userRepository;
        this.excursionRepository = excursionRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.excursionService = excursionService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public String test() {
        System.out.println("CHECK N+1:");

        System.out.println("USER AND HIS EXCURSIONS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(userService.get(1).get().getExcursions());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(userRepository.getWithExcursions(1).get().getExcursions());
        System.out.println("========================\n========================");

        System.out.println("USER AND HIS LIKES:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(userService.get(1).get().getLikes());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(likeRepository.getUserLikes(1));
        System.out.println("========================\n========================");

        System.out.println("USER AND HIS COMMENTS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(userService.get(1).get().getComments());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(commentRepository.getUserComments(1));
        System.out.println("========================\n========================");

        System.out.println("EXCURSION AND ITS USERS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(excursionService.get(1).get().getUsers());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(excursionRepository.getWithUsers(1));
        System.out.println("========================\n========================");

        System.out.println("EXCURSION AND ITS LIKES:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(excursionService.get(1).get().getLikes());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(likeRepository.getExcursionLikes(1));
        System.out.println("========================\n========================");

        System.out.println("EXCURSION AND ITS COMMENTS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(excursionService.get(1).get().getComments());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(commentRepository.getExcursionComments(1));
        System.out.println("========================\n========================");

        return "Проверьте консоль";
    }
}
