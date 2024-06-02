package com.strong.BloodDonation.Model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.Check;

import com.strong.BloodDonation.Utils.AppointmentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "donorId")
    @NotNull
    private Donor donor;

    @NotNull
    @Future(message = "Please Select Future Date")
    private LocalDate appointmentDate;

    @NotNull(message = "Appoint Time Required")
    @Check(constraints = "appointment_time >= '09:00:00' AND appointment_time <= '17:00:00'")
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status Required")
    private AppointmentStatus status;

}
