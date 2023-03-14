package com.sovetnikov.application.controller;

import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.Like;
import com.sovetnikov.application.model.User;
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
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    @Transactional(readOnly = false)
    public String test() {

        User user = new User("Test", "123@test", "1234567890");
        user.setPassword("test");
        userService.create(user);
        Excursion excursion = new Excursion("Test", 0);
        excursionService.create(excursion);

        for (int i = 1; i <= 9; i++) {
            excursionService.addUserToExcursion(i, user.getId());
            likeService.create(new Like(user,excursionService.get(i).get()));
            commentService.create(new Comment("Test",user,excursionService.get(i).get()));

            excursionService.addUserToExcursion(excursion.getId(), i);
            likeService.create(new Like(userService.get(i).get(), excursion));
            commentService.create(new Comment("Test", userService.get(i).get(), excursion));
        }

        System.out.println("CHECK N+1:");

        System.out.println("USER AND HIS EXCURSIONS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(user.getExcursions());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(userRepository.getWithExcursions(user.getId()).get().getExcursions());
        System.out.println("========================\n========================");

        System.out.println("USER AND HIS LIKES:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(user.getLikes());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(likeRepository.getUserLikes(user.getId()));
        System.out.println("========================\n========================");

        System.out.println("USER AND HIS COMMENTS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(user.getComments());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(commentRepository.getUserComments(user.getId()));
        System.out.println("========================\n========================");

        System.out.println("EXCURSION AND ITS USERS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(excursion.getUsers());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(excursionRepository.getWithUsers(excursion.getId()));
        System.out.println("========================\n========================");

        System.out.println("EXCURSION AND ITS LIKES:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(excursion.getLikes());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(likeRepository.getExcursionLikes(excursion.getId()));
        System.out.println("========================\n========================");

        System.out.println("EXCURSION AND ITS COMMENTS:");
        System.out.println("USING DEFAULT INITIALIZATION:");
        System.out.println(excursion.getComments());
        System.out.println("=======================");
        System.out.println("USING APPEAL TO DATA BASE");
        System.out.println(commentRepository.getExcursionComments(excursion.getId()));
        System.out.println("========================\n========================");

        return "Проверьте консоль";
    }
}
