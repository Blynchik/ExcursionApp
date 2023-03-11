package com.sovetnikov.application.controller.ExcursionController;

import com.sovetnikov.application.dto.ExcursionDto;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
import com.sovetnikov.application.util.UserUtils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/excursion")
public class AdminExcursionController {

    private final ExcursionService excursionService;

    @Autowired
    public AdminExcursionController(ExcursionService excursionService) {
        this.excursionService = excursionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExcursionDto> getOne(@PathVariable int id){

        if(excursionService.get(id).isPresent()){
            return ResponseEntity.ok().body(Converter.getExcursionDto(excursionService.get(id).get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
