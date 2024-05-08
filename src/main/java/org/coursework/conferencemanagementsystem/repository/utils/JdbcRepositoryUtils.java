package org.coursework.conferencemanagementsystem.repository.utils;

import org.coursework.conferencemanagementsystem.entity.Entity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Optional;

public final class JdbcRepositoryUtils {

    public static <T extends Entity> Optional<T> saveEntity(NamedParameterJdbcTemplate jdbcTemplate,
                                                            MapSqlParameterSource parameters,
                                                            String query,
                                                            T entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(query, parameters, keyHolder, new String[]{"id"});
        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            entity.setId(generatedId.longValue());
        }

        return Optional.of(entity);
    }

    public static <T extends Entity> Optional<T> getOptionalEntity(NamedParameterJdbcTemplate jdbcTemplate,
                                                           MapSqlParameterSource parameters,
                                                           RowMapper<T> rowMapper,
                                                           String sql) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, parameters, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
