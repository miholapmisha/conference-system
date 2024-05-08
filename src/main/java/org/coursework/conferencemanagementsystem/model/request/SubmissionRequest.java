package org.coursework.conferencemanagementsystem.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.coursework.conferencemanagementsystem.annotation.NotEmptyFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class SubmissionRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Abstraction for paper is required")
    private String abstractOfSubmission;

    @NotEmptyFile(message = "Submission paper is not set")
    private MultipartFile paper;

    List<String> authorsEmails = new ArrayList<>();

    @Size(min = 1, message = "Select at least 1 topic for submission")
    private List<Long> topicsIds = new ArrayList<>();

    private List<Long> pcConflictsIds = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractOfSubmission() {
        return abstractOfSubmission;
    }

    public void setAbstractOfSubmission(String abstractOfSubmission) {
        this.abstractOfSubmission = abstractOfSubmission;
    }

    public MultipartFile getPaper() {
        return paper;
    }

    public void setPaper(MultipartFile paper) {
        this.paper = paper;
    }

    public List<String> getAuthorsEmails() {
        return authorsEmails;
    }

    public void setAuthorsEmails(List<String> authorsEmails) {
        this.authorsEmails = authorsEmails;
    }

    public List<Long> getTopicsIds() {
        return topicsIds;
    }

    public void setTopicsIds(List<Long> topicsIds) {
        this.topicsIds = topicsIds;
    }

    public List<Long> getPcConflictsIds() {
        return pcConflictsIds;
    }

    public void setPcConflictsIds(List<Long> pcConflictsIds) {
        this.pcConflictsIds = pcConflictsIds;
    }
}
