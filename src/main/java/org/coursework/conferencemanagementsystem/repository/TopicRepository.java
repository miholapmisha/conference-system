package org.coursework.conferencemanagementsystem.repository;

import org.coursework.conferencemanagementsystem.entity.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TopicRepository {

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<Topic> rowMapper = (rs, rowNum) -> {
        Topic topic = new Topic();
        topic.setId(rs.getLong("id"));
        topic.setName(rs.getString("name"));
        return topic;
    };

    @Autowired
    public void setNamedJdbcTemplate(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    private static final String SELECT_ALL_TOPICS_QUERY = "SELECT id, name FROM topics LIMIT 500";

    private static final String SELECT_TOPICS_BY_SUBMISSION_ID =
            """
                    SELECT t.id, t.name
                    FROM topics t
                             JOIN submissions_topics st ON t.id = st.topic_id
                             JOIN submissions s ON s.id = st.submission_id
                    WHERE s.id = :submission_id""";

    public List<Topic> getAllTopics() {
        return namedJdbcTemplate.query(SELECT_ALL_TOPICS_QUERY, rowMapper);
    }

    public List<Topic> getTopicsBySubmissionId(Long submissionId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("submission_id", submissionId);

        return namedJdbcTemplate.query(SELECT_TOPICS_BY_SUBMISSION_ID, params, rowMapper);
    }
}
