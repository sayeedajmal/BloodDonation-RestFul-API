package com.strong.BloodDonation.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.strong.BloodDonation.Repository.StaffRepo;

@Service
public class StaffServiceImp implements UserDetailsService {

    private final StaffRepo repository;

    public StaffServiceImp(StaffRepo repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByStaffName(username)
                .orElseThrow(() -> new UsernameNotFoundException("staff not found"));
    }
}