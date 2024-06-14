package com.strong.BloodDonation.Model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.strong.BloodDonation.Utils.Positions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@JsonIgnoreType
public class Staff implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer staffId;

    @NotNull(message = " is Required")
    private String staffName;

    @Enumerated(EnumType.STRING)
    private Positions position;

    @OneToMany(mappedBy = "staff")
    private List<Token> tokens;

    @NotNull(message = " is Required")
    @Min(10)
    private String contactNumber;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @NotNull(message = " is Required")
    private String address;

    @NotNull(message = " is Required")
    private String password;

    @NotNull(message = " is Required")
    private boolean enabled;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    private LocalDateTime updatedAt;

    public Staff(Integer staffId, String staffName, Positions position, String contactNumber, String email,
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (position == null || position.name().isEmpty()) {
            return List.of();
        } else {
            return List.of(new SimpleGrantedAuthority(position.name()));
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return email;

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

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
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
