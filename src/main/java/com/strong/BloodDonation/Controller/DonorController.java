package com.strong.BloodDonation.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strong.BloodDonation.Email.MailService;
import com.strong.BloodDonation.Model.Donor;
import com.strong.BloodDonation.Service.DonorService;
import com.strong.BloodDonation.Utils.BloodException;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

/**
 * DonorController class handles HTTP requests related to donor operations.
 * This controller uses the @RestController annotation to indicate that it's a
 * controller and automatically serializes the returned objects into JSON
 * format.
 */
@RestController
@RequestMapping("/api/v1/donor")
public class DonorController {
    @Autowired
    private MailService mailService;

    @Autowired
    private DonorService donorService;

    /**
     * POST endpoint to create a new donor.
     *
     * @param donor The donor object to be created.
     * @return A response indicating the success or
     *         failure of the operation.
     */
    @PostMapping("createDonor")
    public ResponseEntity<String> createDonor(@Valid @RequestBody Donor donor) throws BloodException {
        donorService.saveDonor(donor);
        mailService.sendDonorSignUpEmail(donor);
        return new ResponseEntity<String>("Created Successfully", HttpStatus.CREATED);
    }

    /**
     * GET endpoint to retrieve a list of all donors.
     *
     * @return A list of donors in JSON format.
     */
    @PreAuthorize("hasAuthority('Donor')")
    @GetMapping("showDonor")
    @Operation(summary = "Get All Donor", description = "Return Donors")
    public ResponseEntity<List<Donor>> showDonor() throws BloodException {
        List<Donor> AllDonor = donorService.findAll();
        return new ResponseEntity<List<Donor>>(AllDonor, HttpStatus.OK);
    }

    /**
     * GET endpoint to retrieve a specific donor by ID.
     *
     * @param donorId The unique identifier of the donor.
     * @return The requested donor in JSON format.
     */
    @PreAuthorize("hasAnyAuthority('Donor','Manager')")
    @GetMapping("{donorId}")
    public ResponseEntity<Donor> showByIdDonor(@PathVariable("donorId") Integer donorId) throws BloodException {
        return new ResponseEntity<Donor>(donorService.findById(donorId), HttpStatus.OK);
    }

    /**
     * DELETE endpoint to delete a donor by ID.
     *
     * @param donorId The unique identifier of the donor to be deleted.
     * @return A response indicating the success or failure of the operation.
     */
    /*
     * @PreAuthorize("hasAuthority('Donor')")
     * 
     * @Transactional
     * 
     * @DeleteMapping("/{donorId}")
     * public ResponseEntity<String> deleteDonor(@PathVariable("donorId") Integer
     * donorId) throws BloodException {
     * donorService.deleteDonor(donorId);
     * return new ResponseEntity<>("Deleted Successfully", HttpStatus.NO_CONTENT);
     * }
     */

    /**
     * PATCH endpoint to update an existing donor.
     *
     * @param updatedDonor The updated donor object.
     * @return A response indicating the success or failure of the operation.
     */
    @PreAuthorize("hasAuthority('Donor')")
    @Transactional
    @PatchMapping("updateDonor")
    public ResponseEntity<String> updateDonor(@RequestBody Donor updatedDonor) {
        try {
            Donor existingDonor = donorService.findById(updatedDonor.getDonorId());
            if (existingDonor != null) {
                existingDonor.setFirstName(updatedDonor.getFirstName());
                existingDonor.setLastName(updatedDonor.getLastName());
                existingDonor.setCity(updatedDonor.getCity());
                existingDonor.setBloodType(updatedDonor.getBloodType());
                existingDonor.setContactNumber(updatedDonor.getContactNumber());
                existingDonor.setDOB(updatedDonor.getDOB());
                existingDonor.setEmail(updatedDonor.getEmail());

                donorService.updateDonor(existingDonor);
                return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("Donor not found", HttpStatus.NOT_FOUND);
        } catch (BloodException e) {
            return new ResponseEntity<>("Failed to update donor: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
