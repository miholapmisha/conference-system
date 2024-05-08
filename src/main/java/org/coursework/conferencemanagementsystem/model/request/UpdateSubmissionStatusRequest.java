package org.coursework.conferencemanagementsystem.model.request;

public class UpdateSubmissionStatusRequest {

    private Long submissionId;

    private String status;

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
