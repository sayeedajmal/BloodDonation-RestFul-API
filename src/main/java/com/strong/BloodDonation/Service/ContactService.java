package com.strong.BloodDonation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strong.BloodDonation.Model.Contact;
import com.strong.BloodDonation.Repository.ContactRepo;
import com.strong.BloodDonation.Utils.BloodException;

@Service
public class ContactService {

    @Autowired
    private ContactRepo contactRepo;

    public void contact(Contact contact) throws BloodException {
        if (contact == null) {
            throw new BloodException("Fill All Fields Correctly");
        } else
            contactRepo.save(contact);
    }
}
