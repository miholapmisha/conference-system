package org.coursework.conferencemanagementsystem.entity.entity_enum;

public enum OverallMerit {
    REJECT(1), WEAK_REJECT(2), WEAK_ACCEPT(3), ACCEPT(4), STRONG_ACCEPT(5);

    private final int value;

    OverallMerit(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
