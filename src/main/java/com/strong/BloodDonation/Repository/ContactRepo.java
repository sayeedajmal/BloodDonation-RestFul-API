package com.strong.BloodDonation.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.strong.BloodDonation.Model.Contact;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer> {

}
