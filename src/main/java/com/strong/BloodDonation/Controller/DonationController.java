package com.strong.BloodDonation.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.strong.BloodDonation.Email.MailService;
import com.strong.BloodDonation.Model.Donation;
import com.strong.BloodDonation.Model.Donor;
import com.strong.BloodDonation.Service.DonationService;
import com.strong.BloodDonation.Service.DonorService;
import com.strong.BloodDonation.Utils.BloodException;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/v1/Donation")
public class DonationController {

    @Autowired
    private MailService mailService;

    @Autowired
    private DonationService donationService;

    @Autowired
    private DonorService donorService;

    /**
     *
     * POST endpoint to create a new Donation.
     *
     * @param donation   The Donation object to be created.
     * @param donationId The unique identifier of the Donation associated with the
     *                   Donation.
     * @return A response indicating the success or failure of the operation.
     */
    @PostMapping("createDonation")
    public ResponseEntity<String> createDonation(@ModelAttribute Donation donation,
            @RequestParam Integer donorId) throws BloodException {

        Donor byId = donorService.findById(donorId);
        donation.setDonor(byId);
        donation.setDonationDate(LocalDate.now());
        donationService.saveDonation(donation);
        mailService.sendDonationConfirmation(donation.getDonor().getEmail(),
                donation.getDonor().getFirstName() + " " + donation.getDonor().getLastName(),
                donation.getQuantity(),
                byId.getBloodType());
        return new ResponseEntity<>("Created Successfully", HttpStatus.CREATED);
    }

    /**
     * GET endpoint to retrieve a list of all Donations.
     *
     * @return A list of Donations in JSON format.
     */
    @GetMapping("showDonation")
    public ResponseEntity<List<Donation>> showDonation() throws BloodException {
        List<Donation> findAll = donationService.findAll();
        return new ResponseEntity<>(findAll, HttpStatus.OK);
    }

    /**
     * GET endpoint to retrieve a specific Donation by ID.
     *
     * @param donationId The unique identifier of the Donation.
     * @return The requested Donation in JSON format.
     */
    @GetMapping("{donationId}")
    public ResponseEntity<Donation> showByIdDonation(@PathVariable("donationId") Integer donationId)
            throws BloodException {
        Donation Donation = donationService.findById(donationId);
        return new ResponseEntity<>(Donation, HttpStatus.OK);
    }

    /**
     * DELETE endpoint to delete an Donation by ID.
     *
     * @param donationId The unique identifier of the Donation to be deleted.
     * @return A response indicating the success or failure of the operation.
     */
    @Transactional
    @DeleteMapping("{donationId}")
    public ResponseEntity<?> deleteDonation(@PathVariable("donationId") Integer donationId)
            throws BloodException {
        donationService.deleteDonation(donationId);
        return new ResponseEntity<>("Sucessfully Deleted", HttpStatus.NO_CONTENT);
    }

    /**
     * PATCH endpoint to update an existing Donation.
     *
     * @param updatedDonation The updated Donation object.
     * @param donationId      The unique identifier of the Donation to be
     *                        updated.
     * @return A response indicating the success or failure of the operation.
     */
    @Transactional
    @PatchMapping("updateDonation")
    public ResponseEntity<String> updateDonation(@RequestBody Donation updatedDonation,
            @RequestParam("donationId") Integer donationId) throws BloodException {
        Donation existDonation = donationService.findById(donationId);
        if (existDonation != null) {
            existDonation.setDonationDate(updatedDonation.getDonationDate());
            existDonation.setDonationStatus(updatedDonation.getDonationStatus());
            existDonation.setQuantity(updatedDonation.getQuantity());
            donationService.updateDonation(existDonation);
            return new ResponseEntity<>("Updated Successfully", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Donation not found", HttpStatus.NOT_FOUND);
    }
}
