package com.strong.BloodDonation.Model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Contact {

    @NotNull(message = " is Required")
    private String Name;

    @NotNull(message = " is Required")
    @Email
    @Id
    private String Email;

    @NotNull(message = " is Required")
    private String Topic;

    @NotNull(message = " is Required")
    private String Message;

}
