package com.sovetnikov.application.service;

import com.sovetnikov.application.config.SecurityConfig;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.Role;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void create(User user) {
        user.setPassword(SecurityConfig.PASSWORD_ENCODER.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
    }

    public Optional<User> get(int id) {
        return userRepository.findById(id);
    }

    public List<User> getAll(int page) {
        return userRepository.findAll(PageRequest.of(page, 3, Sort.by("name"))).getContent();
    }

    @Transactional
    public void update(User updatedUser, int id) {
        updatedUser.setRegisteredAt(userRepository.getReferenceById(id).getRegisteredAt());
        updatedUser.setPassword(userRepository.getReferenceById(id).getPassword());
        updatedUser.setRole(userRepository.getReferenceById(id).getRole());
        updatedUser.setId(id);
        userRepository.save(updatedUser);
    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Transactional
    public void changeAuthority(int id, Role role) {
        userRepository.getReferenceById(id).setRole(role);
    }

    @Transactional
    public void changePassword(int id, String password) {
        userRepository.getReferenceById(id).setPassword(SecurityConfig.PASSWORD_ENCODER.encode(password));
    }

    public List<User> getByNameLike(String query) {
        List<User> list = userRepository.findByNameStartingWithIgnoreCase(query);
        list.sort(Comparator.comparing(User::getName));
        return list;
    }

    public List<Excursion> getWithExcursions(int id, boolean onlyNext) {

        if (onlyNext) {
            return userRepository.getWithExcursions(id).get().getExcursions()
                    .stream()
                    .sorted(Comparator.comparing(Excursion::getDate))
                    .filter(e -> e.getDate().isAfter(LocalDate.now()))
                    .toList();
        }

        return userRepository.getWithExcursions(id).get().getExcursions().stream()
                .sorted(Comparator.comparing(Excursion::getDate))
                .toList();
    }

    public Excursion getTimeTillExcursion(int userId) {
        Excursion excursion = getWithExcursions(userId, true).get(0);
        long excursionDay = excursion.getDate().toEpochDay();
        long todayDay = LocalDate.now().toEpochDay();
        excursion.setDaysTillExcursion(excursionDay - todayDay);
        return excursion;
    }
}
