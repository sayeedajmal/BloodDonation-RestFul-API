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

import lombok.NonNull;

@Service
public class StaffService {

    @Autowired
    private StaffRepo staffRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

/*     public void createStaff(Staff staff) throws BloodException {
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        staff.setEnabled(false);
        staff.setUpdatedAt(null);
        staff.setPosition(null);
        staffRepo.save(staff);
    } */

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

    public void updateStaff(Staff updatedStaff) throws BloodException {
        Staff existingStaff = findById(updatedStaff.getStaffId());
        if (existingStaff != null) {
            existingStaff.setStaffName(updatedStaff.getStaffName());
            existingStaff.setContactNumber(updatedStaff.getContactNumber());
            existingStaff.setEmail(updatedStaff.getEmail());
            existingStaff.setPassword(passwordEncoder.encode(updatedStaff.getPassword()));
            existingStaff.setUpdatedAt(LocalDateTime.now());
        } else
            throw new BloodException("can't find Donor By this Id: ");
    }
}
