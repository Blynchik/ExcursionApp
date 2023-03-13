package com.sovetnikov.application.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExcursionDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExcursionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clearUsers(int id) {
        jdbcTemplate.update("DELETE FROM users_excursion WHERE excursion_id = ?", id);
    }

    public void deleteFromExcursion(int excursionId, int userId) {
        jdbcTemplate.update("DELETE FROM users_excursion WHERE excursion_id = ? AND users_id = ?", excursionId, userId);
    }

    public void addUserToExcursion(int excursionId, int userId){
        jdbcTemplate.update("INSERT INTO users_excursion VALUES(?, ?)", userId, excursionId);
    }
}
