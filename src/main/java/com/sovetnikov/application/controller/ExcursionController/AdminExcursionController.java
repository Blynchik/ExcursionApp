package com.sovetnikov.application.controller.ExcursionController;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.dto.ExcursionDto.BaseExcursionDto;
import com.sovetnikov.application.dto.ExcursionDto.ExcursionDto;
import com.sovetnikov.application.dto.LikeDto;
import com.sovetnikov.application.dto.UserDto.UserDto;
import com.sovetnikov.application.model.*;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.LikeService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.CompetitionTimer;
import com.sovetnikov.application.util.Converter;
import com.sovetnikov.application.util.ErrorList;
import com.sovetnikov.application.util.ExcursionUtils.ExcursionValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jdk.dynalink.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/user/excursion")
public class AdminExcursionController {

    private final ExcursionService excursionService;
    private final ExcursionValidator excursionValidator;
    private final CommentService commentService;
    private final UserService userService;
    private final LikeService likeService;

    @Autowired
    public AdminExcursionController(ExcursionService excursionService,
                                    ExcursionValidator excursionValidator,
                                    CommentService commentService,
                                    UserService userService,
                                    LikeService likeService) {
        this.excursionService = excursionService;
        this.excursionValidator = excursionValidator;
        this.commentService = commentService;
        this.userService = userService;
        this.likeService = likeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExcursionDto> getOne(@PathVariable int id) {

        Optional<Excursion> excursion = excursionService.get(id);

        if (excursion.isPresent()) {
            ExcursionDto excursionDto = Converter.getExcursionDto(excursion.get());

            excursionDto.setUsers(excursionService.getWithUsers(id).stream()
                    .map(u -> {
                        UserDto user = Converter.getUserDto(u);
                        user.setPhoneNumber(u.getPhoneNumber());
                        user.setEmail(u.getEmail());
                        return user;
                    }).toList());

            excursionDto.setLikesAmount(excursionService.getLikesAmount(id));

            excursionDto.setDate(excursion.get().getDate());

            return ResponseEntity.ok().body(excursionDto);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping()
    public ResponseEntity<Object> createExcursion(@Valid @RequestBody BaseExcursionDto excursionDto,
                                                  BindingResult bindingResult) {

        excursionValidator.validate(excursionDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        Excursion excursion = Converter.getExcursion(excursionDto);
        excursionService.create(excursion);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<ExcursionDto>> delete(@PathVariable int id) {

        if (excursionService.get(id).isPresent()) {
            excursionService.delete(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id,
                                         @Valid @RequestBody BaseExcursionDto excursionDto,
                                         BindingResult bindingResult) {

        excursionValidator.validate(excursionDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        if (excursionService.get(id).isPresent()) {
            Excursion excursion = Converter.getExcursion(excursionDto);
            excursionService.update(excursion, id);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{id}/clear")
    public ResponseEntity<HttpStatus> clearExcursionUsers(@PathVariable int id) {

        if (excursionService.get(id).isPresent()) {

            excursionService.clearExcursionUsers(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/exclusion")
    public ResponseEntity<HttpStatus> excludeUser(@PathVariable int id,
                                                  @RequestParam int userId) {

        Optional<User> user = userService.get(userId);

        if (user.isPresent()
                && excursionService.get(id).isPresent()
                && excursionService.getWithUsers(id).contains(user.get())) {

            excursionService.deleteFromExcursion(id, userId);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/add")
    public ResponseEntity<HttpStatus> addUser(@PathVariable int id,
                                              @RequestParam int userId) {

        Optional<User> user = userService.get(userId);

        if (user.isPresent() && excursionService.get(id).isPresent()) {
            if (!excursionService.getWithUsers(id).contains(user.get())){
                excursionService.addUserToExcursion(id, userId);

                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/zeroing")
    public ResponseEntity<HttpStatus> zeroingLikes(@PathVariable int id) {

        if (excursionService.get(id).isPresent()) {
            likeService.clearAllLikes(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getWinner")
    public ResponseEntity<List<ExcursionDto>> getWinner() {
        if (CompetitionTimer.timeToCompare()) {

            List<ExcursionDto> excursionDtoList = new ArrayList<>();

            for (Excursion excursion : excursionService.getWinner()) {

                ExcursionDto excursionDto = Converter.getExcursionDto(excursion);
                excursionDto.setLikesAmount(likeService.getExcursionLikes(excursion.getId()).size());
                excursionDto.setDate(excursion.getDate());
                excursionDtoList.add(excursionDto);
                likeService.clearAllLikes(excursion.getId());
            }

            return ResponseEntity.ok().body(excursionDtoList.stream()
                    .sorted(Comparator.comparing(ExcursionDto::getLikesAmount).reversed())
                    .collect(Collectors.toList()));
        }

        return ResponseEntity.noContent().build();
    }
}
