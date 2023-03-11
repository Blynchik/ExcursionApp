package com.sovetnikov.application.service;

import com.sovetnikov.application.config.SecurityConfig;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.Role;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.repository.ExcursionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ExcursionService {

    private final ExcursionRepository excursionRepository;

    @Autowired
    public ExcursionService(ExcursionRepository excursionRepository) {
        this.excursionRepository = excursionRepository;
    }

    @Transactional
    public void create(Excursion excursion){
        excursionRepository.save(excursion);
    }

    public Optional<Excursion> get(int id) {
        return excursionRepository.findById(id);
    }

    public List<Excursion> getAll() {
        return excursionRepository.findAll();
    }

    @Transactional
    public void update(Excursion updatedExcursion, int id) {
        updatedExcursion.setId(id);
        excursionRepository.save(updatedExcursion);
    }

    @Transactional
    public void delete(int id) {
        excursionRepository.deleteById(id);
    }

    public List<Excursion> getByNameLike(String query){
        return excursionRepository.findByNameStartingWithIgnoreCase(query);
    }

    public List<User> getWithUsers(int id) {
        return excursionRepository.getWithUsers(id).get().getUsers();
    }
}
