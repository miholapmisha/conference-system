package org.coursework.conferencemanagementsystem.entity;

import org.coursework.conferencemanagementsystem.entity.entity_enum.SubmissionStatus;

import java.util.Objects;

public class Submission extends Entity {

    private String title;

    private String abstractOfSubmission;

    private String fileDestination;

    private SubmissionStatus status;

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

    public String getFileDestination() {
        return fileDestination;
    }

    public void setFileDestination(String fileDestination) {
        this.fileDestination = fileDestination;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Submission that)) return false;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getAbstractOfSubmission(), that.getAbstractOfSubmission()) && Objects.equals(getFileDestination(), that.getFileDestination()) && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getAbstractOfSubmission(), getFileDestination(), getStatus());
    }
}

