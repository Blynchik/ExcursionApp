package com.sovetnikov.application.service;

import com.sovetnikov.application.aspect.LogExecutionTime;
import com.sovetnikov.application.dao.ExcursionDao;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.repository.ExcursionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ExcursionService {

    private final ExcursionRepository excursionRepository;
    private final LikeService likeService;
    private final ExcursionDao excursionDao;

    @Autowired
    public ExcursionService(ExcursionRepository excursionRepository,
                            ExcursionDao excursionDao,
                            LikeService likeService) {
        this.excursionRepository = excursionRepository;
        this.excursionDao = excursionDao;
        this.likeService = likeService;
    }

    @LogExecutionTime
    @Transactional
    public void create(Excursion excursion) {
        excursionRepository.save(excursion);
    }

    @LogExecutionTime
    public Optional<Excursion> get(int id) {
        return excursionRepository.findById(id);
    }

    @LogExecutionTime
    public List<Excursion> getAll(int page, boolean onlyNext) {

        if (onlyNext) {

            List<Excursion> excursions = excursionRepository.findAll(Sort.by("date"))
                    .stream()
                    .filter(e -> e.getDate().isAfter(LocalDate.now()))
                    .toList();

            return new PageImpl<Excursion>(excursions, PageRequest.of(page, 3), excursions.size()).getContent();
        }

        return excursionRepository.findAll(PageRequest.of(page, 3, Sort.by("date"))).getContent();
    }

    @LogExecutionTime
    @Transactional
    public void update(Excursion updatedExcursion, int id) {
        updatedExcursion.setId(id);
        excursionRepository.save(updatedExcursion);
    }

    @LogExecutionTime
    @Transactional
    public void delete(int id) {
        excursionRepository.deleteById(id);
    }

    @LogExecutionTime
    public List<Excursion> getByNameLike(String query) {
        List<Excursion> list = excursionRepository.findByNameStartingWithIgnoreCase(query);
        list.sort(Comparator.comparing(Excursion::getName));
        return list;
    }

    @LogExecutionTime
    public List<User> getWithUsers(int id) {
        List<User> list = excursionRepository.getWithUsers(id).get().getUsers();
        list.sort(Comparator.comparing(User::getName));
        return list;
    }

    @LogExecutionTime
    @Transactional
    public void clearExcursionUsers(int id) {
        excursionDao.clearUsers(id);
    }

    @LogExecutionTime
    @Transactional
    public void deleteFromExcursion(int excursionId, int userId) {
        excursionDao.deleteFromExcursion(excursionId, userId);
    }

    @LogExecutionTime
    @Transactional
    public void addUserToExcursion(int excursionId, int userId) {
        excursionDao.addUserToExcursion(excursionId, userId);
    }

    @LogExecutionTime
    public List<Excursion> getWinner() {

        return excursionRepository.findAll(Sort.by("date")).stream()
                .filter(e -> e.getDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(Excursion::getLikesAmount).reversed())
                .toList();
    }

    @LogExecutionTime
    public int getLikesAmount(int excursionId) {
        return likeService.getExcursionLikes(excursionId).size();
    }
}

