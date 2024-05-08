package org.coursework.conferencemanagementsystem.entity;

import org.coursework.conferencemanagementsystem.entity.entity_enum.OverallMerit;

import java.time.LocalDateTime;

public class Review extends Entity {

    private String fileDestination;

    private Long submissionId;

    private String comment;

    private Long authorId;

    private OverallMerit overallMerit;

    private LocalDateTime reviewDate;

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getFileDestination() {
        return fileDestination;
    }

    public void setFileDestination(String fileDestination) {
        this.fileDestination = fileDestination;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public OverallMerit getOverallMerit() {
        return overallMerit;
    }

    public void setOverallMerit(OverallMerit overallMerit) {
        this.overallMerit = overallMerit;
    }
}
