package org.coursework.conferencemanagementsystem.repository;

import org.coursework.conferencemanagementsystem.entity.Submission;
import org.coursework.conferencemanagementsystem.entity.entity_enum.SubmissionStatus;
import org.coursework.conferencemanagementsystem.repository.utils.JdbcRepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SubmissionRepository {

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<Submission> submissionRowMapper = (rs, rowNum) -> {
        Submission submission = new Submission();
        submission.setId(rs.getLong("id"));
        submission.setAbstractOfSubmission(rs.getString("abstract"));
        submission.setTitle(rs.getString("title"));
        submission.setStatus(SubmissionStatus.valueOf(rs.getString("status")));
        submission.setFileDestination(rs.getString("file_destination"));

        return submission;
    };

    private static final String SAVE_SUBMISSION_QUERY =
            """
                    INSERT INTO submissions(abstract,title,status,file_destination)\s
                    VALUES (:abstract,:title,:status,:file_destination)""";

    private static final String SAVE_SUBMISSION_TO_PROGRAM_COMMITTEE_RELATIONSHIP_QUERY
            = "INSERT INTO assigns(program_committee_id,submission_id) VALUES (:program_committee_id,:submission_id)";

    private static final String SAVE_SUBMISSION_AUTHOR_RELATIONSHIP_QUERY
            = "INSERT INTO submissions_authors(author_id, submission_id) VALUES (:author_id, :submission_id)";

    private static final String SAVE_SUBMISSION_TOPIC_RELATIONSHIP_QUERY
            = "INSERT INTO submissions_topics(topic_id, submission_id) VALUES (:topic_id, :submission_id)";

    private static final String SAVE_SUBMISSION_CONFLICT_RELATIONSHIP_QUERY
            = "INSERT INTO submissions_conflicts(account_id, submission_id) VALUES (:account_id, :submission_id)";

    private static final String SELECT_SUBMISSION_BY_ID_QUERY
            = "SELECT id, title, abstract, file_destination, status FROM submissions WHERE id = :id";

    private static final String SELECT_SUBMISSION_BY_AUTHOR_EMAIL_QUERY
            = """
            SELECT s.id, s.title, s.abstract, s.file_destination, s.status
            FROM submissions s
                     JOIN submissions_authors sa ON s.id = sa.submission_id
                     JOIN accounts a ON sa.author_id = a.id
            WHERE a.email = :email""";

    private static final String SELECT_SUBMISSION_ASSIGNED_PROGRAM_COMMITTEE_QUERY
            = """
            SELECT s.id, s.title, s.abstract, s.file_destination, s.status
            FROM submissions s
                     JOIN assigns a on s.id = a.submission_id
                     JOIN accounts a2 on a2.id = a.program_committee_id
            WHERE a2.id = :program_committee_id
              AND NOT EXISTS(SELECT 1
                             FROM reviews
                             WHERE reviews.submission_id = s.id
                               AND reviews.author_id = a2.id)""";

    private static final String SELECT_ALL_SUBMISSIONS_QUERY =
            "SELECT s.id, s.title, s.abstract, s.file_destination, s.status FROM submissions s ORDER BY s.id";

    private static final String UPDATE_SUBMISSION_STATUS_BY_ID_QUERY =
            "UPDATE submissions SET status = :status WHERE id = :id";

    private static final String DELETE_SUBMISSION_TO_PROGRAM_COMMITTEE_RELATIONSHIP_QUERY =
            "DELETE FROM assigns WHERE program_committee_id = :program_committee_id AND submission_id = :submission_id";

    private static final String SELECT_SUBMISSIONS_BY_SEARCH_REQUEST =
            """
                    SELECT *
                    FROM submissions
                    WHERE title ILIKE CONCAT('%', :search, '%')
                       OR abstract ILIKE CONCAT('%', :search, '%')
                       OR file_destination ILIKE CONCAT('%', :search, '%')""";

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedJdbcTemplate = jdbcTemplate;
    }

    public Optional<Submission> saveSubmission(Submission submission) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("abstract", submission.getAbstractOfSubmission())
                .addValue("title", submission.getTitle())
                .addValue("status", String.valueOf(submission.getStatus()))
                .addValue("file_destination", submission.getFileDestination());

        return JdbcRepositoryUtils.saveEntity(namedJdbcTemplate, parameters, SAVE_SUBMISSION_QUERY, submission);
    }

    public void insertSubmissionToAuthor(Long submissionId, Long authorId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("submission_id", submissionId)
                .addValue("author_id", authorId);

        namedJdbcTemplate.update(SAVE_SUBMISSION_AUTHOR_RELATIONSHIP_QUERY, parameters);
    }

    public void insertSubmissionToTopic(Long submissionId, Long topicId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("submission_id", submissionId)
                .addValue("topic_id", topicId);
        namedJdbcTemplate.update(SAVE_SUBMISSION_TOPIC_RELATIONSHIP_QUERY, parameters);
    }

    public void insertSubmissionToConflictAuthor(Long submissionId, Long conflictAuthorId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("submission_id", submissionId)
                .addValue("account_id", conflictAuthorId);

        namedJdbcTemplate.update(SAVE_SUBMISSION_CONFLICT_RELATIONSHIP_QUERY, parameters);
    }

    public void insertSubmissionToProgramCommittee(Long programCommitteeId, Long submissionId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("submission_id", submissionId)
                .addValue("program_committee_id", programCommitteeId);

        namedJdbcTemplate.update(SAVE_SUBMISSION_TO_PROGRAM_COMMITTEE_RELATIONSHIP_QUERY, parameters);
    }

    public List<Submission> getAllSubmissionsByAuthorEmail(String email) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);

        return namedJdbcTemplate.query(SELECT_SUBMISSION_BY_AUTHOR_EMAIL_QUERY, parameters, submissionRowMapper);
    }

    public List<Submission> getSubmissionAssignedToProgramCommittee(Long programCommitteeId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("program_committee_id", programCommitteeId);

        return namedJdbcTemplate.query(SELECT_SUBMISSION_ASSIGNED_PROGRAM_COMMITTEE_QUERY, parameters, submissionRowMapper);
    }

    public Optional<Submission> getSubmissionById(Long id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return JdbcRepositoryUtils
                .getOptionalEntity(namedJdbcTemplate, parameters, submissionRowMapper, SELECT_SUBMISSION_BY_ID_QUERY);
    }

    public List<Submission> getAllSubmissions() {
        return namedJdbcTemplate.query(SELECT_ALL_SUBMISSIONS_QUERY, submissionRowMapper);
    }

    public void updateStatusBySubmissionId(Long submissionId, SubmissionStatus submissionStatus) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", submissionId)
                .addValue("status", String.valueOf(submissionStatus));

        namedJdbcTemplate.update(UPDATE_SUBMISSION_STATUS_BY_ID_QUERY, parameters);
    }

    public void removeSubmissionToProgramCommittee(Long pcId, Long submissionId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("program_committee_id", pcId)
                .addValue("submission_id", submissionId);
        namedJdbcTemplate.update(DELETE_SUBMISSION_TO_PROGRAM_COMMITTEE_RELATIONSHIP_QUERY, parameters);
    }

    public List<Submission> getSubmissionsBySearchRequest(String searchRequest) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("search", searchRequest);

        return namedJdbcTemplate.query(SELECT_SUBMISSIONS_BY_SEARCH_REQUEST, parameters, submissionRowMapper);
    }
}
