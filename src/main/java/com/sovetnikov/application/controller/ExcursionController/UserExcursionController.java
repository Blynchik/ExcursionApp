package com.sovetnikov.application.controller.ExcursionController;

import com.sovetnikov.application.dto.ExcursionDto;
import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.AuthUser;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
import com.sovetnikov.application.util.ExcursionUtils.ExcursionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/excursion")
public class UserExcursionController {

    private final ExcursionService excursionService;
    private final UserService userService;

    @Autowired
    public UserExcursionController(ExcursionService excursionService, UserService userService) {
        this.excursionService = excursionService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExcursionDto> getOne(@PathVariable int id) {

        if (excursionService.get(id).isPresent()) {
            return ResponseEntity.ok().body(Converter.getExcursionDto(excursionService.get(id).get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<ExcursionDto>> getAll() {
        return ResponseEntity.ok().body(excursionService.getAll().stream()
                .map(Converter::getExcursionDto).toList());
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<ExcursionDto>> getByName(@RequestParam String query) {
        return ResponseEntity.ok().body(excursionService.getByNameLike(query).stream()
                .map(Converter::getExcursionDto).toList());
    }

    @GetMapping("/own")
    public ResponseEntity<List<ExcursionDto>> getOwnExcursions(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok().body(
                userService.getWithExcursions(authUser.id()).stream()
                        .map(Converter::getExcursionDto).toList());
    }
}
