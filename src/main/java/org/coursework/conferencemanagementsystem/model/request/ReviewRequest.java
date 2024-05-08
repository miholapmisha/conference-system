package org.coursework.conferencemanagementsystem.model.request;


import jakarta.validation.constraints.NotNull;
import org.coursework.conferencemanagementsystem.annotation.OverallMeritValue;
import org.springframework.web.multipart.MultipartFile;

public class ReviewRequest {

    @OverallMeritValue(message = "Your estimation is not selected")
    private String overallMerit;

    private MultipartFile reviewPaper;

    @NotNull
    private String comment;

    private Long submissionId;

    public String getOverallMerit() {
        return overallMerit;
    }

    public void setOverallMerit(String overallMerit) {
        this.overallMerit = overallMerit;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public MultipartFile getReviewPaper() {
        return reviewPaper;
    }

    public void setReviewPaper(MultipartFile reviewPaper) {
        this.reviewPaper = reviewPaper;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
