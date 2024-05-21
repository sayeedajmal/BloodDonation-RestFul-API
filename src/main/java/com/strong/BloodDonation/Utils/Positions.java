package com.strong.BloodDonation.Utils;

public enum Positions {
    Appoint("APPOINT STAFF"),
    Donor("DONOR STAFF"),
    Nurse("NURSE/Phlebotomist"),
    Manager("Manager");

    private final String displayValue;

    Positions(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
