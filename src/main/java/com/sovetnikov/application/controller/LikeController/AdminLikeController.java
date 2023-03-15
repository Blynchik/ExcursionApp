package com.sovetnikov.application.controller.LikeController;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.dto.LikeDto;
import com.sovetnikov.application.model.AuthUser;
import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.model.Like;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.LikeService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class AdminLikeController {

    private final LikeService likeService;
    private final ExcursionService excursionService;
    private final UserService userService;

    @Autowired
    public AdminLikeController(LikeService likeService,
                               ExcursionService excursionService,
                               UserService userService) {
        this.likeService = likeService;
        this.excursionService = excursionService;
        this.userService = userService;
    }

    @Operation(summary = "Доступна только администратору. " +
            "Возвращает все лайки пользователя (имя пользователя, название экскурсии) или " +
            "пустой список. Ответ 200, если id существует, иначе 404")
    @GetMapping("/{id}/like")
    public ResponseEntity<List<LikeDto>> getAllUserLikes(@PathVariable int id) {

        if (userService.get(id).isPresent()) {
            return ResponseEntity.ok().body(likeService.getUserLikes(id).stream()
                    .map(Converter::getLikeDto).toList());
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Доступна только администратору. Удаялет любой лайк." +
    "Ответ 200, если лайк существует, иначе 404")
    @DeleteMapping("/like/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id) {

        if (likeService.get(id).isPresent()) {
            likeService.delete(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
