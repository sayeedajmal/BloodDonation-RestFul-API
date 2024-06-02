package com.strong.BloodDonation.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.strong.BloodDonation.Model.Donor;
import com.strong.BloodDonation.Model.MedicalHistory;
import com.strong.BloodDonation.Service.DonorService;
import com.strong.BloodDonation.Service.MedicalHistoryService;
import com.strong.BloodDonation.Utils.BloodException;

import jakarta.transaction.Transactional;

/**
 * MedicalHistController class handles HTTP requests related to medical history
 * operations.
 * This controller uses the @RestController annotation to indicate that it's a
 * controller and automatically serializes the returned objects into JSON
 * format.
 */
@RestController
@RequestMapping("/api/v1/medicalHistory")
public class MedicalHistController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private DonorService donorService;

    @Autowired
    private MailService mailService;

    /**
     * POST endpoint to create a new medical history.
     *
     * @param medicalHistory The medical history object to be created.
     * @param donorId        The unique identifier of the donor associated with the
     *                       medical history.
     * @return A response indicating the success or failure of the operation.
     */
    @PreAuthorize("hasAuthority('Donor')")
    @PostMapping("createHistory")
    public ResponseEntity<String> createHistory(@ModelAttribute MedicalHistory medicalHistory,
            @RequestParam("donorId") Integer donorId) throws BloodException {
        Donor donor = donorService.findById(donorId);
        if (donor != null) {
            medicalHistory.setDonor(donor);
            medicalHistoryService.saveHistory(medicalHistory);

            mailService.sendMedicalHistoryNotification(medicalHistory);
            return new ResponseEntity<>("Created Successfully", HttpStatus.CREATED);
        } else {
            throw new BloodException("Can't Find Donor with ID: " + donorId);
        }
    }

    /**
     * GET endpoint to retrieve a list of all medical histories.
     *
     * @return A list of medical histories in JSON format.
     * @throws BloodException
     */
    @PreAuthorize("hasAnyAuthority('Donor','Nurse')")
    @GetMapping("showHistory")
    public ResponseEntity<List<MedicalHistory>> showHistory() throws BloodException {
        return new ResponseEntity<>(medicalHistoryService.findAll(), HttpStatus.OK);
    }

    /**
     * GET endpoint to retrieve a specific medical history by ID.
     *
     * @param historyId The unique identifier of the medical history.
     * @return The requested medical history in JSON format.
     */
    @PreAuthorize("hasAnyAuthority('Donor','Nurse')")
    @GetMapping("{historyId}")
    public ResponseEntity<MedicalHistory> showByIdHistory(@PathVariable("historyId") Integer historyId)
            throws BloodException {
        MedicalHistory history = medicalHistoryService.findById(historyId);
        return new ResponseEntity<MedicalHistory>(history, HttpStatus.OK);
    }

    /**
     * GET endpoint to retrieve medical history by donor ID.
     *
     * @param donorId The unique identifier of the donor.
     * @return The medical history associated with the donor in JSON format.
     */
    @PreAuthorize("hasAnyAuthority('Donor','Nurse')")
    @GetMapping("findByDonor/{donorId}")
    public ResponseEntity<MedicalHistory> findByDonorId(@PathVariable("donorId") Integer donorId)
            throws BloodException {
        MedicalHistory history = medicalHistoryService.findByDonorId(donorId);
        return new ResponseEntity<MedicalHistory>(history, HttpStatus.OK);
    }

    /**
     * DELETE endpoint to delete a medical history by ID.
     *
     * @param historyId The unique identifier of the medical history to be deleted.
     * @return A response indicating the success or failure of the operation.
     */

    @PreAuthorize("hasAuthority('Donor')")
    @Transactional
    @DeleteMapping("{historyId}")
    public ResponseEntity<?> deleteHistory(@PathVariable("historyId") Integer historyId) throws BloodException {
        medicalHistoryService.deleteHistory(historyId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.NO_CONTENT);
    }

    /**
     * PATCH endpoint to update an existing medical history.
     *
     * @param history The updated medical history object.
     * @return A response indicating the success or failure of the operation.
     */
    @PreAuthorize("hasAuthority('Donor')")
    @Transactional
    @PatchMapping("updateHistory")
    public ResponseEntity<String> updateHistory(@RequestBody MedicalHistory history) throws BloodException {
        MedicalHistory existingHistory = medicalHistoryService.findById(history.getHistoryId());
        if (existingHistory != null) {
            existingHistory.setAllergies(history.getAllergies());
            existingHistory.setMedicalCondition(history.getMedicalCondition());
            existingHistory.setMedications(history.getMedications());

            medicalHistoryService.updateHistory(existingHistory);

            mailService.sendMedicalHistoryUpdatedNotification(existingHistory);
            return new ResponseEntity<>("Updated Successfully", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Medical history not found", HttpStatus.NOT_FOUND);
    }
}
