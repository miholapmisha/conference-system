package org.coursework.conferencemanagementsystem.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ConferenceInformation {

    private String name;

    private String deadline;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getFormattedDeadline() {
        String dateString = getDeadline();
        LocalDateTime dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM dd, yyyy, h a 'UTC'", Locale.ENGLISH);
        return dateTime.format(formatter);
    }

    public LocalDateTime getLocalDateDeadline() {
        return LocalDateTime.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
    }
}
