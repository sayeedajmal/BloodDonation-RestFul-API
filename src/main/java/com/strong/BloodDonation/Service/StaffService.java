package com.strong.BloodDonation.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.strong.BloodDonation.Model.Staff;
import com.strong.BloodDonation.Repository.StaffRepo;
import com.strong.BloodDonation.Utils.BloodException;
import com.strong.BloodDonation.Utils.Positions;

import lombok.NonNull;

@Service
public class StaffService {

    @Autowired
    private StaffRepo staffRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
     * public void createStaff(Staff staff) throws BloodException {
     * staff.setPassword(passwordEncoder.encode(staff.getPassword()));
     * staff.setEnabled(false);
     * staff.setUpdatedAt(null);
     * staff.setPosition(null);
     * staffRepo.save(staff);
     * }
     */

    public Staff findByEmail(String email) {
        return staffRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email Not Found " + email));
    }

    public Staff findById(@NonNull Integer staffId) throws BloodException {
        Staff staff = staffRepo.findById(staffId).orElse(null);
        if (staff != null) {
            return staff;
        } else
            throw new BloodException("Staff Is Not Found with " + staffId);
    }

    public List<Staff> findAll() throws BloodException {
        List<Staff> staff = staffRepo.findAll();
        if (!staff.isEmpty()) {
            return staff;
        } else
            throw new BloodException("There's No Staff");
    }

    public void deleteStaff(Integer staffId) throws BloodException {
        Staff staff = findById(staffId);
        if (staff != null) {
            try {
                staffRepo.delete(staff);
            } catch (Exception e) {
                throw new BloodException("Error deleting Donor : " + e.getLocalizedMessage());
            }
        } else
            throw new BloodException("can't find Donor By this Id: " + staffId, new Throwable());
    }

    public Staff updateStaffPosition(Integer staffId, Positions positions, boolean enabled) throws BloodException {
        Staff byId = findById(staffId);
        if (byId != null) {
            byId.setEnabled(enabled);
            byId.setPosition(positions);
            return staffRepo.save(byId);
        } else {
            throw new BloodException("Can't find Staff by this Id: " + staffId);
        }

    }

    public void updateStaff(Staff updatedStaff) throws BloodException {
        Staff existingStaff = findById(updatedStaff.getStaffId());
        if (existingStaff != null) {
            if (updatedStaff.getStaffName() != null && !updatedStaff.getStaffName().isEmpty()) {
                existingStaff.setStaffName(updatedStaff.getStaffName());
            }
            if (updatedStaff.getContactNumber() != null && !updatedStaff.getContactNumber().isEmpty()) {
                existingStaff.setContactNumber(updatedStaff.getContactNumber());
            }
            if (updatedStaff.getEmail() != null && !updatedStaff.getEmail().isEmpty()) {
                existingStaff.setEmail(updatedStaff.getEmail());
            }
            if (updatedStaff.getPassword() != null && !updatedStaff.getPassword().isEmpty()) {
                existingStaff.setPassword(passwordEncoder.encode(updatedStaff.getPassword()));
            }

            existingStaff.setUpdatedAt(LocalDateTime.now());
            staffRepo.save(existingStaff);

        } else {
            throw new BloodException("Can't find Staff by this Id: " + updatedStaff.getStaffId());
        }
    }

}
