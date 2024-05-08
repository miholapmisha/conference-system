package org.coursework.conferencemanagementsystem.model.request;

import java.util.ArrayList;
import java.util.List;

public class AssignSubmissionRequest {

    private Long submissionId;

    private List<Long> programCommitteesIds = new ArrayList<>();

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public List<Long> getProgramCommitteesIds() {
        return programCommitteesIds;
    }

    public void setProgramCommitteesIds(List<Long> programCommitteesIds) {
        this.programCommitteesIds = programCommitteesIds;
    }
}
