package com.sovetnikov.application.controller.ExcursionController;

import com.sovetnikov.application.dto.ExcursionDto;
import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
import com.sovetnikov.application.util.ErrorList;
import com.sovetnikov.application.util.ExcursionUtils.ExcursionValidator;
import com.sovetnikov.application.util.UserUtils.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/excursion")
public class AdminExcursionController {

    private final ExcursionService excursionService;
    private final ExcursionValidator excursionValidator;

    @Autowired
    public AdminExcursionController(ExcursionService excursionService, ExcursionValidator excursionValidator) {
        this.excursionService = excursionService;
        this.excursionValidator = excursionValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExcursionDto> getOne(@PathVariable int id){

        if(excursionService.get(id).isPresent()){
            return ResponseEntity.ok().body(Converter.getExcursionDto(excursionService.get(id).get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<ExcursionDto>> getAll(){
        return ResponseEntity.ok().body(excursionService.getAll().stream()
                .map(Converter::getExcursionDto).toList());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody ExcursionDto excursionDto,
                                         BindingResult bindingResult){

        excursionValidator.validate(excursionDto,bindingResult);

        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        Excursion excursion = Converter.getExcursion(excursionDto);
        excursionService.create(excursion);

        return ResponseEntity.ok().body(Converter.getExcursionDto(excursion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<ExcursionDto>> delete(@PathVariable int id){

        if (excursionService.get(id).isPresent()){
            excursionService.delete(id);
            return ResponseEntity.ok().body(excursionService.getAll().stream()
                    .map(Converter::getExcursionDto).toList());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id,
                                         @Valid @RequestBody ExcursionDto excursionDto,
                                         BindingResult bindingResult){

        excursionValidator.validate(excursionDto, bindingResult);

        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        if (excursionService.get(id).isPresent()) {
            Excursion excursion = Converter.getExcursion(excursionDto);
            excursionService.update(excursion, id);

            return ResponseEntity.ok().body(Converter.getExcursionDto(excursionService.get(id).get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<ExcursionDto>> getByName(@RequestParam String query) {
        return ResponseEntity.ok().body(excursionService.getByNameLike(query).stream()
                .map(Converter::getExcursionDto).toList());
    }
}
