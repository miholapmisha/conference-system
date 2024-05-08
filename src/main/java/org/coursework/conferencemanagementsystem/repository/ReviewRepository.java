package org.coursework.conferencemanagementsystem.repository;

import org.coursework.conferencemanagementsystem.entity.Review;
import org.coursework.conferencemanagementsystem.entity.entity_enum.OverallMerit;
import org.coursework.conferencemanagementsystem.repository.utils.JdbcRepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepository {

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<Review> reviewRowMapper = (rs, rowNum) -> {
        Review review = new Review();
        review.setId(rs.getLong("id"));
        review.setComment(rs.getString("comment"));
        review.setOverallMerit(OverallMerit.valueOf(rs.getString("overall_merit")));
        review.setFileDestination(rs.getString("file_destination"));
        review.setAuthorId(rs.getLong("author_id"));
        review.setSubmissionId(rs.getLong("submission_id"));
        review.setReviewDate(rs.getTimestamp("review_date").toLocalDateTime());

        return review;
    };

    private final static String SAVE_REVIEW_QUERY =
            """
                    INSERT INTO reviews(submission_id,comment,author_id,overall_merit,file_destination, review_date) 
                    VALUES(:submission_id, :comment, :author_id, :overall_merit, :file_destination, :review_date)""";

    private final static String SELECT_REVIEW_BY_ID_QUERY =
            """
                    SELECT id, submission_id, comment, author_id, overall_merit, file_destination, review_date 
                    FROM reviews 
                    WHERE id = :id""";

    private final static String SELECT_REVIEWS_BY_AUTHOR_ID_QUERY =
            """
                    SELECT id, submission_id, comment, author_id, overall_merit, file_destination, review_date 
                    FROM reviews 
                    WHERE author_id = :author_id 
                    ORDER BY review_date DESC """;

    private final static String SELECT_REVIEW_BY_AUTHOR_AND_SUBMISSION_IDS_QUERY =
            """
                    SELECT id, submission_id, comment, author_id, overall_merit, file_destination, review_date 
                    FROM reviews 
                    WHERE author_id = :author_id AND submission_id = :submission_id""";

    private final static String SELECT_REVIEWS_BY_SUBMISSION_ID  =
            """
                    SELECT id, submission_id, comment, author_id, overall_merit, file_destination, review_date 
                    FROM reviews 
                    WHERE submission_id = :submission_id""";

    private final static String UPDATE_REVIEW_QUERY =
            """
                    UPDATE reviews
                    SET comment          = :comment,
                        author_id        = :author_id,
                        overall_merit    = :overall_merit,
                        file_destination = :file_destination,
                        review_date      = :review_date
                    WHERE id = :id""";

    private final static String DELETE_REVIEW_BY_ID_QUERY = "DELETE FROM reviews WHERE id=:review_id";

    @Autowired
    public void setNamedJdbcTemplate(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public Optional<Review> saveReview(Review review) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("submission_id", review.getSubmissionId())
                .addValue("comment", review.getComment())
                .addValue("author_id", review.getAuthorId())
                .addValue("overall_merit", String.valueOf(review.getOverallMerit()))
                .addValue("file_destination", review.getFileDestination())
                .addValue("review_date", Timestamp.valueOf(review.getReviewDate()));

        return JdbcRepositoryUtils.saveEntity(namedJdbcTemplate, params, SAVE_REVIEW_QUERY, review);
    }

    public List<Review> getReviewsByAuthor(Long authorId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("author_id", authorId);

        return namedJdbcTemplate.query(SELECT_REVIEWS_BY_AUTHOR_ID_QUERY, params, reviewRowMapper);
    }

    public Optional<Review> getReviewBySubmissionAndReviewAuthorIds(Long submissionId, Long authorId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("submission_id", submissionId)
                .addValue("author_id", authorId);
        return JdbcRepositoryUtils.getOptionalEntity(namedJdbcTemplate, params, reviewRowMapper,
                SELECT_REVIEW_BY_AUTHOR_AND_SUBMISSION_IDS_QUERY);
    }

    public Optional<Review> getReviewById(Long reviewId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", reviewId);

        return JdbcRepositoryUtils.getOptionalEntity(namedJdbcTemplate, parameters, reviewRowMapper, SELECT_REVIEW_BY_ID_QUERY);
    }

    public void updateReview(Review review, Long reviewId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", reviewId)
                .addValue("comment", review.getComment())
                .addValue("author_id", review.getAuthorId())
                .addValue("overall_merit", String.valueOf(review.getOverallMerit()))
                .addValue("file_destination", review.getFileDestination())
                .addValue("review_date", Timestamp.valueOf(review.getReviewDate()));

        namedJdbcTemplate.update(UPDATE_REVIEW_QUERY, params);
    }

    public void deleteReviewById(Long reviewId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId);
        namedJdbcTemplate.update(DELETE_REVIEW_BY_ID_QUERY, params);
    }

    public List<Review> getReviewBySubmissionId(Long submissionId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("submission_id", submissionId);

        return namedJdbcTemplate.query(SELECT_REVIEWS_BY_SUBMISSION_ID, params, reviewRowMapper);
    }
}
