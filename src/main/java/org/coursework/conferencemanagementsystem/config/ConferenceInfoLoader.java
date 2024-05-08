package org.coursework.conferencemanagementsystem.config;

import org.coursework.conferencemanagementsystem.model.ConferenceInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@Configuration
public class ConferenceInfoLoader {

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private final static String GET_CONFERENCE_INFO_QUERY = "SELECT deadline, name FROM conference_information";

    @Autowired
    public void setNamedJdbcTemplate(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Bean
    public ConferenceInformation conferenceInformation() {
        ConferenceInformation conferenceInfo = new ConferenceInformation();

        SqlRowSet rowSet = namedJdbcTemplate.queryForRowSet(GET_CONFERENCE_INFO_QUERY, new MapSqlParameterSource());

        if (rowSet.next()) {
            conferenceInfo.setName(rowSet.getString("name"));
            conferenceInfo.setDeadline(rowSet.getString("deadline"));
        }

        return conferenceInfo;
    }
}
