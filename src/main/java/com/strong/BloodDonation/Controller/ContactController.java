package com.strong.BloodDonation.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strong.BloodDonation.Email.MailService;
import com.strong.BloodDonation.Model.Contact;
import com.strong.BloodDonation.Service.ContactService;
import com.strong.BloodDonation.Utils.BloodException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ContactController {

    @Autowired
    private MailService mailService;
    @Autowired
    private ContactService contactService;

    @PostMapping("contactOrg")
    public ResponseEntity<?> contact(@Valid @RequestBody Contact contact) throws BloodException {
        contactService.contact(contact);
        mailService.sendContactEmail(contact);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
