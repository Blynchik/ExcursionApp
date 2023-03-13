package com.sovetnikov.application.service;

import com.sovetnikov.application.config.SecurityConfig;
import com.sovetnikov.application.dao.ExcursionDao;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.Role;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.repository.ExcursionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ExcursionService {

    private final ExcursionRepository excursionRepository;
    private final ExcursionDao excursionDao;

    @Autowired
    public ExcursionService(ExcursionRepository excursionRepository, ExcursionDao excursionDao) {
        this.excursionRepository = excursionRepository;
        this.excursionDao = excursionDao;
    }

    @Transactional
    public void create(Excursion excursion) {
        excursionRepository.save(excursion);
    }

    public Optional<Excursion> get(int id) {
        return excursionRepository.findById(id);
    }

    public List<Excursion> getAll(int page, boolean onlyNext) {

        if (onlyNext) {

            List<Excursion> excursions= excursionRepository.findAll( Sort.by("date"))
                    .stream()
                    .filter(e -> e.getDate().isAfter(LocalDate.now()))
                    .toList();

            return new PageImpl<Excursion>(excursions, PageRequest.of(page, 3), excursions.size()).getContent();
        }

        return excursionRepository.findAll(PageRequest.of(page, 3, Sort.by("date"))).getContent();
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

    public List<Excursion> getByNameLike(String query) {
        List<Excursion> list = excursionRepository.findByNameStartingWithIgnoreCase(query);
        list.sort(Comparator.comparing(Excursion::getName));
        return list;
    }

    public List<User> getWithUsers(int id) {
        List<User> list = excursionRepository.getWithUsers(id).get().getUsers();
        list.sort(Comparator.comparing(User::getName));
        return list;
    }

    @Transactional
    public void clearExcursionUsers(int id) {
        excursionDao.clearUsers(id);
    }

    @Transactional
    public void deleteFromExcursion(int excursionId, int userId) {
        excursionDao.deleteFromExcursion(excursionId, userId);
    }

    @Transactional
    public void addUserToExcursion(int excursionId, int userId) {
        excursionDao.addUserToExcursion(excursionId, userId);
    }
}

