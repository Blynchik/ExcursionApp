package com.sovetnikov.application.controller.LikeController;

import com.sovetnikov.application.dto.LikeDto;
import com.sovetnikov.application.model.AuthUser;
import com.sovetnikov.application.model.Like;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.LikeService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/own")
    public ResponseEntity<List<LikeDto>> getAllUserLikes(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok().body(likeService.getUserLikes(authUser.id()).stream()
                .map(Converter::getLikeDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@AuthenticationPrincipal AuthUser authUser,
                                             @PathVariable int id) {

        if (likeService.get(id).isPresent()) {
            if (likeService.get(id).get().getUser().getId() == authUser.id()) {
                likeService.delete(id);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
