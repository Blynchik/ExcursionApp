package com.sovetnikov.application.controller.ExcursionController;

import com.sovetnikov.application.dto.ExcursionDto.BaseExcursionDto;
import com.sovetnikov.application.dto.ExcursionDto.ExcursionDto;
import com.sovetnikov.application.dto.UserDto.UserDto;
import com.sovetnikov.application.model.*;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.LikeService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.CompetitionTimer;
import com.sovetnikov.application.util.Converter;
import com.sovetnikov.application.util.ErrorList;
import com.sovetnikov.application.util.ExcursionUtils.ExcursionValidator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final UserService userService;
    private final LikeService likeService;

    @Autowired
    public AdminExcursionController(ExcursionService excursionService,
                                    ExcursionValidator excursionValidator,
                                    UserService userService,
                                    LikeService likeService) {
        this.excursionService = excursionService;
        this.excursionValidator = excursionValidator;
        this.userService = userService;
        this.likeService = likeService;
    }

    @Operation(summary = "Доступна только администратору. " +
            "Возвращает по id название экскурсии, дату ее проведения, " +
            "количество лайков и список всех пользователей (в алфавитном порядке), которые на нее записаны" +
            " с контактными даннымми. Ответ 200, если id существует, иначе 404")
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

    @Operation(summary = "Доступна только администратору. " +
            "Создает новую экскурсию. Ограничения. " +
            "Название всегда должно начинаться с заглавной буквы." +
            "Название должно состоять от 2 до 100 символов." +
            "Описание - не превышать более 500 символов, может быть пустым." +
            "Цена не должна быть отрицательнной. " +
            "Дата должна соответствовать формату. " +
            "Ответ 200, при ошибке - 400.")
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

    @Operation(summary = "Доступна только администратору. Удаляет экскурсию по id." +
            "Все ее лайки удаляются, список записанных на нее пользователей очищается, " +
            "комментарии к этой экскурсии остаются, но в названии эскурсии будет null." +
            "Ответ 200, если id существует, иначе 404")
    @DeleteMapping("/{id}")
    public ResponseEntity<List<ExcursionDto>> delete(@PathVariable int id) {

        if (excursionService.get(id).isPresent()) {
            excursionService.delete(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Доступна только администратору. " +
            "Полностью изменяет данные эккскурсии по id. Ограничения, " +
            "такие же как и при создании экскурсии. Ответ 200, если id существует, " +
            "иначе 404. При ошибке - 400")
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

    @Operation(summary = "Доступна только администратору. " +
            "Полностью очищает список пользователей, записанных на экскурсию по ее id. " +
            "Ответ 200, если id существует, иначе 404")
    @PatchMapping("/{id}/clear")
    public ResponseEntity<HttpStatus> clearExcursionUsers(@PathVariable int id) {

        if (excursionService.get(id).isPresent()) {

            excursionService.clearExcursionUsers(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Доступна только администратору. " +
            "Исключает из экскурсии (id) пользователя (userId). " +
            "Если пользователь существует и состоит в существующей экскурсии - " +
            "ответ 200, иначе 404")
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

    @Operation(summary = "Доступна только администратору. " +
            "Записывает пользователя (userId) на экскурсию (id), " +
            "если пользователь и экскурсия существуют. Дублирование пользователей " +
            "пресекается - ответ 400. Ответ 200, если id не существуют, то - 404")
    @PatchMapping("/{id}/add")
    public ResponseEntity<HttpStatus> addUser(@PathVariable int id,
                                              @RequestParam int userId) {

        Optional<User> user = userService.get(userId);

        if (user.isPresent() && excursionService.get(id).isPresent()) {
            if (!excursionService.getWithUsers(id).contains(user.get())) {
                excursionService.addUserToExcursion(id, userId);

                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Доступна только администратору. " +
            "Обнуляет лайки экскурсии по ее id. " +
            "Ответ 200, если экскурсия существует, иначе 404")
    @PatchMapping("/{id}/zeroing")
    public ResponseEntity<HttpStatus> zeroingLikes(@PathVariable int id) {

        if (excursionService.get(id).isPresent()) {
            likeService.clearAllLikes(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Доступна только для администратора. " +
            "Возвращает список прошедших экскурсий с их названием и датой" +
            " проведения. Список отсортирован по количеству лайков " +
            "от большего к меньшему. После выявления победителя " +
            "лайки обнуляются для для прошедших экскурсий. На данный момент" +
            " определять победителя можно каждый день. Период голосования можно " +
            "изменить в com.sovetnikov.application.CompetitionTimer. " +
            "Если период соревнования еще не истек, то ответ 204." +
            "При удачном запросе - 200")
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
