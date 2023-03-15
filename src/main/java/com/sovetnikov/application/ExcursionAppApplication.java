package com.sovetnikov.application;

import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.model.Like;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.LikeService;
import com.sovetnikov.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class ExcursionAppApplication implements ApplicationRunner {

    private final UserService userService;
    private final ExcursionService excursionService;
    private final LikeService likeService;
    private final CommentService commentService;

    @Autowired
    public ExcursionAppApplication(UserService userService,
                                   ExcursionService excursionService,
                                   LikeService likeService,
                                   CommentService commentService) {
        this.userService = userService;
        this.excursionService = excursionService;
        this.likeService = likeService;
        this.commentService = commentService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExcursionAppApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<String> names = new ArrayList<>(List.of("Михаил Александров", "Михаил Александров", "Никита", "Арсений", "Маргарита ",
                "Никита", "Алиса", "Татьяна", "Филипп", "Кристина", "Милана", "Дмитрий", "Ксения", "Иван", "Мария", "Ева", "Варвара",
                "Тимофей", "Мирослава", "София", "Даниил", "Павел", "Матвей", "Илья", "Александра", "Алексей", "Ярослав", "Сергей", "Татьяна"));

        List<String> messages = new ArrayList<>(List.of("Великолепно!", "Отлично!", "Понравилось!", "Нормально!", "Пойдет", "Не понравилось"));

        for (int i = 0; i < 10; i++) {
            User user = new User(
                    names.get((int) (Math.random() * names.size())) + i,
                    i + "@email.ru", "987654" + (4000 - i));
            user.setPassword("password");
            userService.create(user);
        }

        excursionService.addUserToExcursion(2, 2);
        excursionService.addUserToExcursion(3, 2);
        excursionService.addUserToExcursion(4, 3);
        excursionService.addUserToExcursion(5, 4);
        excursionService.addUserToExcursion(6, 5);
        excursionService.addUserToExcursion(7, 6);
        excursionService.addUserToExcursion(8, 7);
        excursionService.addUserToExcursion(2, 8);
        excursionService.addUserToExcursion(8, 8);
        excursionService.addUserToExcursion(3, 7);
        excursionService.addUserToExcursion(4, 6);
        excursionService.addUserToExcursion(5, 5);
        excursionService.addUserToExcursion(6, 4);
        excursionService.addUserToExcursion(7, 3);
        excursionService.addUserToExcursion(8, 2);
        excursionService.addUserToExcursion(6, 1);
        excursionService.addUserToExcursion(1, 8);
        excursionService.addUserToExcursion(2, 7);
        excursionService.addUserToExcursion(3, 6);
        excursionService.addUserToExcursion(4, 5);
        excursionService.addUserToExcursion(5, 8);
        excursionService.addUserToExcursion(6, 6);
        excursionService.addUserToExcursion(7, 5);
        excursionService.addUserToExcursion(8, 4);

        likeService.create(new Like(userService.get(2).get(), excursionService.get(2).get()));
        likeService.create(new Like(userService.get(3).get(), excursionService.get(2).get()));
        likeService.create(new Like(userService.get(4).get(), excursionService.get(2).get()));
        likeService.create(new Like(userService.get(5).get(), excursionService.get(2).get()));
        likeService.create(new Like(userService.get(6).get(), excursionService.get(5).get()));
        likeService.create(new Like(userService.get(7).get(), excursionService.get(6).get()));
        likeService.create(new Like(userService.get(8).get(), excursionService.get(7).get()));
        likeService.create(new Like(userService.get(9).get(), excursionService.get(8).get()));
        likeService.create(new Like(userService.get(2).get(), excursionService.get(8).get()));
        likeService.create(new Like(userService.get(3).get(), excursionService.get(7).get()));
        likeService.create(new Like(userService.get(4).get(), excursionService.get(6).get()));
        likeService.create(new Like(userService.get(5).get(), excursionService.get(5).get()));
        likeService.create(new Like(userService.get(6).get(), excursionService.get(4).get()));
        likeService.create(new Like(userService.get(7).get(), excursionService.get(3).get()));
        likeService.create(new Like(userService.get(8).get(), excursionService.get(2).get()));
        likeService.create(new Like(userService.get(9).get(), excursionService.get(1).get()));
        likeService.create(new Like(userService.get(1).get(), excursionService.get(8).get()));
        likeService.create(new Like(userService.get(2).get(), excursionService.get(7).get()));
        likeService.create(new Like(userService.get(3).get(), excursionService.get(6).get()));
        likeService.create(new Like(userService.get(4).get(), excursionService.get(5).get()));
        likeService.create(new Like(userService.get(5).get(), excursionService.get(8).get()));
        likeService.create(new Like(userService.get(6).get(), excursionService.get(6).get()));
        likeService.create(new Like(userService.get(7).get(), excursionService.get(5).get()));
        likeService.create(new Like(userService.get(8).get(), excursionService.get(4).get()));

        for (int i = 0; i < 20; i++) {
            Comment comment = new Comment(
                    messages.get((int) (Math.random() * messages.size())),
                    userService.get((int) (Math.random() * 10) + 1).get(),
                    excursionService.get((int) (Math.random() * 8) + 1).get());

            commentService.create(comment);
        }
    }
}
