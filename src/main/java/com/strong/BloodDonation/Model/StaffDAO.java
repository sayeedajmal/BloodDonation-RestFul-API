package com.strong.BloodDonation.Model;

import java.sql.Date;
import java.time.LocalDateTime;

import com.strong.BloodDonation.Utils.Positions;

public class StaffDAO {

    private Integer staffId;
    private String staffName;

    private Positions position;

    private String contactNumber;

    private String email;

    private String address;

    private boolean enabled;

    private Date createdAt;

    private LocalDateTime updatedAt;

    public StaffDAO(Integer staffId, String staffName, Positions position, String contactNumber, String email,
            String address, boolean enabled, Date createdAt, LocalDateTime updatedAt) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.position = position;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Positions getPosition() {
        return position;
    }

    public void setPosition(Positions position) {
        this.position = position;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
