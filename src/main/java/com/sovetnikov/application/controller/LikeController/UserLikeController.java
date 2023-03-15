package com.sovetnikov.application.controller.LikeController;

import com.sovetnikov.application.dto.LikeDto;
import com.sovetnikov.application.model.AuthUser;
import com.sovetnikov.application.model.Like;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.LikeService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/like")
public class UserLikeController {

    private final LikeService likeService;
    private final ExcursionService excursionService;
    private final UserService userService;

    @Autowired
    public UserLikeController(LikeService likeService,
                               ExcursionService excursionService,
                               UserService userService) {
        this.likeService = likeService;
        this.excursionService = excursionService;
        this.userService = userService;
    }

    @Operation(summary = "Доступна всем зарегестрированным пользователям. " +
            "Возвращает все лайки (имя пользователя, название экскурсии) " +
            "авторизованного пользователя. Ответ 200, если пользователь существует," +
            " иначе 404.")
    @GetMapping("/own")
    public ResponseEntity<List<LikeDto>> getAllUserLikes(@AuthenticationPrincipal AuthUser authUser) {

        if (userService.get(authUser.id()).isPresent()) {

            return ResponseEntity.ok().body(likeService.getUserLikes(authUser.id()).stream()
                    .map(Converter::getLikeDto).toList());
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Доступна всем зарегистрированным пользователям. " +
            "Удаляет лайк по id. Если лайк принадлежит другому пользователю, " +
            "то ответ 400, если id не существует - 404. При удачной " +
            "попытке - 200")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@AuthenticationPrincipal AuthUser authUser,
                                             @PathVariable int id) {

        Optional<Like> like = likeService.get(id);

        if (like.isPresent()) {
            if (like.get().getUser().getId() == authUser.id()) {
                likeService.delete(id);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
