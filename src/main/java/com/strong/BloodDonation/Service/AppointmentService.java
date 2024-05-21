package com.strong.BloodDonation.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strong.BloodDonation.Model.Appointment;
import com.strong.BloodDonation.Model.Donor;
import com.strong.BloodDonation.Repository.AppointRepo;
import com.strong.BloodDonation.Repository.DonorRepo;
import com.strong.BloodDonation.Utils.BloodException;

import lombok.NonNull;

@Service
public class AppointmentService {

    @Autowired
    private AppointRepo appointRepo;

    @Autowired
    private DonorRepo donorRepo;

    public void saveAppointment(Appointment appointment) throws BloodException {
        if (appointment.getAppointmentTime() != null && appointment.getAppointmentDate() != null) {

            if (!isWithinBusinessHours(appointment.getAppointmentTime())) {
                throw new BloodException("Appointment time must be within business hours.");
            }
            try {
                appointRepo.save(appointment);
            } catch (Exception e) {
                throw new BloodException("Error saving appointment", e);
            }
        } else
            throw new BloodException("Enter Correctly Your Date and Time");
    }

    public Appointment findById(@NonNull Integer id) throws BloodException {
        return appointRepo.findById(id)
                .orElseThrow(() -> new BloodException("Can't Find Appointment with " + id + " Appointment ID"));
    }

    public List<Appointment> findAll() throws BloodException {
        List<Appointment> findAll = appointRepo.findAll();
        if (findAll.isEmpty()) {
            throw new BloodException("There isn't Any Appointment List");
        }
        return findAll;
    }

    public void deleteAppointment(Integer appointId) throws BloodException {
        try {
            appointRepo.delete(findById(appointId));
        } catch (Exception e) {
            throw new BloodException("Error deleting appointment", e);
        }
    }

    public void updateAppointment(Appointment appointment) throws BloodException {
        if (appointment.getAppointmentId() != null) {
            if (appointment.getAppointmentTime() == null) {
                throw new BloodException("Invalid appointment data");
            }
            try {
                appointRepo.save(appointment);
            } catch (Exception e) {
                throw new BloodException("Error updating appointment", e);
            }
        } else {
            throw new BloodException("Invalid Appointment ID: ");
        }
    }

    public List<Donor> doAppointDonor() throws BloodException {
        List<Integer> todayAppointments = appointRepo.doAppointDonor();
        List<Donor> donorList = new ArrayList<>();

        for (Integer donorId : todayAppointments) {
            donorList.add(donorRepo.findById(donorId).orElse(null));
        }
        if (!donorList.isEmpty()) {
            return donorList;
        } else
            throw new BloodException("Chill ! There isn't Any Donor TO Draw The Blood");
    }

    public List<Appointment> todayAppointments() throws BloodException {
        List<Appointment> todayAppointments = appointRepo.todayAppointments(LocalDate.now());
        if (!todayAppointments.isEmpty()) {
            return todayAppointments;
        } else
            throw new BloodException("There isn't Any Appointment Today");
    }

    public List<Appointment> findAppointByDate(LocalDate date) throws BloodException {
        List<Appointment> findAppoint = appointRepo.findAppointByDate(date);

        if (!findAppoint.isEmpty()) {
            return findAppoint;
        } else
            throw new BloodException(
                    "There isn't Any Appoint In " + date.format(DateTimeFormatter.ofPattern("u MMM yyyy")));
    }

    /* ====================LOGIC Timing For Appointments====================== */
    private boolean isWithinBusinessHours(LocalTime appointmentTime) {
        LocalTime businessStart = LocalTime.of(9, 0, 0);
        LocalTime businessEnd = LocalTime.of(17, 0, 0);
        return !appointmentTime.isBefore(businessStart) &&
                !appointmentTime.isAfter(businessEnd);
    }

}
