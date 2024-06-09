package com.strong.BloodDonation.Email;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.strong.BloodDonation.Model.Appointment;
import com.strong.BloodDonation.Model.Contact;
import com.strong.BloodDonation.Model.Donation;
import com.strong.BloodDonation.Model.Donor;
import com.strong.BloodDonation.Model.MedicalHistory;
import com.strong.BloodDonation.Model.Staff;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;

/**
 * Service class for sending emails for various events in the Blood Donation
 * application.
 */
@Service
@EnableAsync
public class MailService {

  @Value("${bloodDonation.organisation.name}")
  private String OrganisationName;

  @Value("${bloodDonation.organisation.Email}")
  private String OrganisationEmail;
  @Value("${bloodDonation.organisation.website}")
  private String OrganisationWebsite;

  @Value("${bloodDonation.organisation.PhoneNumber}")
  private String OrganisationPhone;

  @Value("${bloodDonation.organisation.Location}")
  private String OrganisationLocation;

  @Autowired
  private JavaMailSender emailSender;

  /**
   * Sends a MimeMessage email with the provided details.
   * 
   * @param to      the recipient email address
   * @param subject the subject of the email
   * @param text    the content of the email
   */
  @Async
  public void sendEmail(@NonNull String to, @NonNull String subject, @NonNull String htmlContent) {
    try {
      MimeMessage message = emailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);

      emailSender.send(message);
    } catch (MessagingException e) {
      System.out.println("Failed to send email: " + e.getMessage());
    }
  }

  @Async
  public void sendContactEmail(Contact contact) {
    String subject = "New Contact Message from " + contact.getName();
    String htmlContent = "<html><head><style>"
        + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }"
        + ".container { background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }"
        + "h1 { text-align: center; font-size: 24px; color: #333; }"
        + "p { line-height: 1.5; color: #666; }"
        + "</style></head><body>"
        + "<div class='container'>"
        + "<h1>New Contact Message</h1>"
        + "<p><b>Name:</b> " + contact.getName() + "</p>"
        + "<p><b>Email:</b> " + contact.getEmail() + "</p>"
        + "<p><b>Topic:</b> " + contact.getTopic() + "</p>"
        + "<p><b>Message:</b></p>"
        + "<p>" + contact.getMessage() + "</p>"
        + "</div>"
        + "</body></html>";

    sendEmail(OrganisationEmail, subject, htmlContent);
  }

  @Async
  public void sendDonorSignUpEmail(Donor donor) {
    String subject = "Welcome to Blood Donation Portal";
    String htmlContent = "<html><head><style>"
        + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }"
        + ".container { background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }"
        + "h1 { text-align: center; font-size: 24px; color: #333; }"
        + "p { line-height: 1.5; color: #666; }"
        + ".heart { color: #e74c3c; font-size: 2em; margin-right: 5px; }"
        + ".button { display: block; background-color: #e74c3c; color: #fff; padding: 10px 20px; border: none; border-radius: 5px; text-align: center; cursor: pointer; margin-top: 20px; text-decoration: none; }"
        + ".button:hover { background-color: #c0392b; }"
        + "</style></head><body>"
        + "<div class='container'>"
        + "<h1><span class='heart'>&hearts;</span> You're a Lifesaver!</h1>"
        + "<p>Dear <b>" + donor.getFirstName() + " " + donor.getLastName() + "</b>, Your ID : <b>" + donor.getDonorId()
        + "</b>,</p>"
        + "<p>From the bottom of our hearts, thank you for choosing to donate blood! Your generous act will touch the lives of countless individuals in need.</p>"
        + "<p>Your decision doesn't just fill a vial, it fills their hope, their laughter, and their second chances. You are a true hero!</p>"
        + "<p>We will send you a separate email with your appointment confirmation and details once you have scheduled your donation.</p>"
        + "<p>In the meantime, you can visit our website to learn more about the heroes saving lives every day and how you can be a part of it:</p>"
        + "<p><a href='" + OrganisationWebsite + "' class='button'>Visit our website</a></p>"
        + "<p>Thank you again for making a difference!</p>"
        + "<p>Sincerely,<br/>" + OrganisationName + "Team. </p>"
        + "</div>"
        + "</body></html>";
    sendEmail(donor.getEmail(), subject, htmlContent);
  }

  @Async
  public void sendAppointmentNotification(Appointment appointment) {
    String subject = "Appointment Confirmation";
    String htmlContent = "<html><head><style>"
        + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }"
        + ".container { background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }"
        + "h1 { text-align: center; font-size: 24px; color: #333; }"
        + "p { line-height: 1.5; color: #666; }"
        + ".heart { color: #e74c3c; font-size: 2em; margin-right: 5px; }"
        + ".info { list-style: none; padding: 0; }"
        + ".info li { margin-bottom: 10px; }"
        + ".button { display: block; background-color: #e74c3c; color: #fff; padding: 10px 20px; border: none; border-radius: 5px; text-align: center; cursor: pointer; margin-top: 20px; text-decoration: none; }"
        + ".button:hover { background-color: #c0392b; }"
        + "</style></head><body>"
        + "<div class='container'>"
        + "<h1> <span class='heart'>&hearts;</span> Your Lifesaving Appointment is Set!</h1>"
        + "<p>Dear <b>" + appointment.getDonor().getFirstName() + " " + appointment.getDonor().getLastName()
        + "</b>,</p>"
        + "<p>Your Appointment ID : <b>" + appointment.getAppointmentId() + "</b></p>"
        + "<p>We're pleased to confirm your appointment for blood donation.</p>"
        + "<p>Here are the details:</p>"
        + "<ul class='info'>"
        + "<li><b>Date and Time:</b> "
        + appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("d MMM uuuu")) + " at "
        + appointment.getAppointmentTime().format(DateTimeFormatter.ISO_LOCAL_TIME)
        + "</li>"
        + "<li><b>Location:</b> " + OrganisationLocation + "</li>"
        + "<li><b>What to bring: Please bring your medical history records. </b></li>"
        + "</ul>"
        + "<p>If you need to reschedule your appointment, please contact us at " + OrganisationPhone
        + " or reply to this email.</p>"
        + "<p>See you soon!</p>"
        + "<p>Sincerely,<br/>" + OrganisationName + " Team. </p>"
        + "</div>"
        + "</body></html>";
    sendEmail(appointment.getDonor().getEmail(), subject, htmlContent);
  }

  @Async
  public void sendUpdateAppointNotification(Appointment appointment) {
    String subject = "Appointment Update Notification";
    String htmlContent = "<html><head><style>"
        + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }"
        + ".container { background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }"
        + "h1 { text-align: center; font-size: 24px; color: #333; }"
        + "p { line-height: 1.5; color: #666; }"
        + ".heart { color: #e74c3c; font-size: 2em; margin-right: 5px; }"
        + ".info { list-style: none; padding: 0; }"
        + ".info li { margin-bottom: 10px; }"
        + ".button { display: block; background-color: #e74c3c; color: #fff; padding: 10px 20px; border: none; border-radius: 5px; text-align: center; cursor: pointer; margin-top: 20px; text-decoration: none; }"
        + ".button:hover { background-color: #c0392b; }"
        + "</style></head><body>"
        + "<div class='container'>"
        + "<h1> <span class='heart'>&hearts;</span> Your Lifesaving Appointment has been Updated!</h1>"
        + "<p>Dear <b>" + appointment.getDonor().getFirstName() + " " + appointment.getDonor().getLastName()
        + "</b>,</p>"
        + "<p>We're pleased to inform you that your appointment details have been updated.</p>"
        + "<p>Here are the updated details:</p>"
        + "<ul class='info'>"
        + "<li><b>Date and Time:</b> "
        + appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("d MMM uuuu")) + " at "
        + appointment.getAppointmentTime().format(DateTimeFormatter.ISO_LOCAL_TIME)
        + "</li>"
        + "<li><b>Location:</b> " + OrganisationLocation + "</li>"
        + "<li><b>Status:</b> " + appointment.getStatus() + "</li>"
        + "</ul>"
        + "<p>If you have any questions or need further assistance, please contact us at " + OrganisationPhone
        + " or reply to this email.</p>"
        + "<p>Thank you!</p>"
        + "<p>Sincerely,<br/>" + OrganisationName + " Team. </p>"
        + "</div>"
        + "</body></html>";
    sendEmail(appointment.getDonor().getEmail(), subject, htmlContent);
  }

  @Async
  public void sendMedicalHistoryNotification(MedicalHistory history) {
    String subject = "Medical History Created";
    String htmlContent = "<html><head><style>"
        + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }"
        + ".container { background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }"
        + "h1 { text-align: center; font-size: 24px; color: #333; }"
        + "p { line-height: 1.5; color: #666; }"
        + "h3 { text-align: center; color: #333; }"
        + ".heart { color: #e74c3c; font-size: 2em; margin-right: 5px; }"
        + ".button { display: block; background-color: #e74c3c; color: #fff; padding: 10px 20px; border: none; border-radius: 5px; text-align: center; cursor: pointer; margin-top: 20px; text-decoration: none; }"
        + ".button:hover { background-color: #c0392b; }"
        + "</style></head><ubody>"
        + "<div class='container'>"
        + "<h1><span class='heart'>&hearts;</span> Medical History Created</h1>"
        + "<p>Dear " + history.getDonor().getFirstName() + ",</p>"
        + "<p>Your medical history has been successfully recorded. This information will help us provide better care for you in the future.</p>"
        + "<h3>Your Medical History</h3>"
        + "<p><b>Medical Condition:</b>" + history.getMedicalCondition() + "</p>"
        + "<p><b>Medications:</b> " + history.getMedications() + "</p>"
        + "<p><b>Allergies:</b>" + history.getAllergies().toString() + "</p>"
        + "<p>If you have any questions or concerns regarding your medical history, please feel free to reach out to us.</p>"
        + "<p>Best regards,</p>"
        + "<p>The Blood Donation Team</p>"
        + "</div>"
        + "</body></html>";

    sendEmail(history.getDonor().getEmail(), subject, htmlContent);
  }

  @Async
  public void sendMedicalHistoryUpdatedNotification(MedicalHistory history) {
    String subject = "Medical History Updated";
    String htmlContent = "<html><head><style>"
        + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }"
        + ".container { background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }"
        + "h1 { text-align: center; font-size: 24px; color: #333; }"
        + "p { line-height: 1.5; color: #666; }"
        + "h3 { text-align: center; color: #333; }"
        + ".heart { color: #e74c3c; font-size: 2em; margin-right: 5px; }"
        + ".button { display: block; background-color: #e74c3c; color: #fff; padding: 10px 20px; border: none; border-radius: 5px; text-align: center; cursor: pointer; margin-top: 20px; text-decoration: none; }"
        + ".button:hover { background-color: #c0392b; }"
        + "</style></head><ubody>"
        + "<div class='container'>"
        + "<h1><span class='heart'>&hearts;</span> Medical History Updated</h1>"
        + "<p>Dear " + history.getDonor().getFirstName() + ",</p>"
        + "<p>Your medical history has been successfully updated. This information will help us provide better care for you in the future.</p>"
        + "<h3>Your Medical History</h3>"
        + "<p><b>Updated Medical Condition:</b>" + history.getMedicalCondition() + "</p>"
        + "<p><b>Updated Medications:</b> " + history.getMedications() + "</p>"
        + "<p><b>Updated Allergies:</b>" + history.getAllergies().toString() + "</p>"
        + "<p>If you have any questions or concerns regarding your medical history, please feel free to reach out to us.</p>"
        + "<p>Best regards,</p>"
        + "<p>The Blood Donation Team</p>"
        + "</div>"
        + "</body></html>";

    sendEmail(history.getDonor().getEmail(), subject, htmlContent);
  }

  @Async
  public void sendStaffWelcomeEmail(Staff staff) {
    String subject = "Welcome to Our Team!";
    String htmlContent = "<html><head><style>"
        + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }"
        + ".container { background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }"
        + "h1 { text-align: center; font-size: 24px; color: #333; }"
        + "p { line-height: 1.5; color: #666; }"
        + "h4 {text-align: center; font-size: 18px; color: #0663440}"
        + ".heart { color: #e74c3c; font-size: 2em; margin-right: 5px; }"
        + ".button { display: block; background-color: #e74c3c; color: #fff; padding: 10px 20px; border: none; border-radius: 5px; text-align: center; cursor: pointer; margin-top: 20px; text-decoration: none; }"
        + ".button:hover { background-color: #c0392b; }"
        + "</style></head><body>"
        + "<div class='container'>"
        + "<h1><span class='heart'>&hearts;</span> Welcome to Our Team, " + staff.getStaffName() + "!</h1><br>"
        + " <h2>Your Id is:-  " + staff.getStaffId() + "</h2>"
        + "<p>We are thrilled to have you on board. Your account has been successfully created.</p>"
        + "<p>Feel free to explore our platform and reach out if you have any questions or need assistance.</p>"
        + "<h4> <b> We've notified you about your position, please wait for further instructions.</b></h4>"
        + "<p>Best regards,</p>"
        + "<p>The " + OrganisationName + " Manager.</p>"
        + "</div>"
        + "</body></html>";

    sendEmail(staff.getEmail(), subject, htmlContent);
  }

  @Async
  public void sendStaffPositionNotification(Staff staff) {
    String subject = "Staff Position Update";
    String status = staff.isEnabled() ? "Active" : "Deactive";

    String htmlContent = "<html><head><style>"
        + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }"
        + ".container { background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }"
        + "h1 { text-align: center; font-size: 24px; color: #333; }"
        + "p { line-height: 1.5; color: #666; }"
        + "h4 {text-align: center; font-size: 18px; color: #0663440}"
        + ".heart { color: #e74c3c; font-size: 2em; margin-right: 5px; }"
        + ".button { display: block; background-color: #e74c3c; color: #fff; padding: 10px 20px; border: none; border-radius: 5px; text-align: center; cursor: pointer; margin-top: 20px; text-decoration: none; }"
        + ".button:hover { background-color: #c0392b; }"
        + "</style></head><body>"
        + "<div class='container'>"
        + "<h1><span class='heart'>&hearts;</span> Staff Position Updated Successfully!</h1>"
        + "<p>Dear " + staff.getStaffName() + " ID: " + staff.getStaffId() + ",</p>"
        + "<h4>We're delighted to inform you that you've been assigned the position of <b>" + staff.getPosition()
        + "</b>.</h4>"
        + "<p>Thank you for your dedication and commitment to our team. We believe you'll excel in your new role!</p>"
        + "<h4>Your position has been successfully updated. You are now <b><i>" + status + "</i></b>.</h4>"
        + "<p>Best regards,</p>"
        + "<p>The " + OrganisationName + " Manager</p>"
        + "</div>"
        + "</body></html>";

    sendEmail(staff.getEmail(), subject, htmlContent);
  }

  @Async
  public void sendDonationConfirmation(Donation donation) {
    String subject = "Blood Donation Confirmation";
    String htmlContent = "<html><head><style>"
        + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }"
        + ".container { background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }"
        + "h1 { text-align: center; font-size: 24px; margin-bottom: 10px; color: #333; }"
        + "p { line-height: 1.5; color: #666; }"
        + ".heart { color: #e74c3c; font-size: 2em; margin-right: 5px; }"
        + ".blood-details { font-weight: bold; }"
        + ".button { display: block; background-color: #e74c3c; color: #fff; padding: 10px 20px; border: none; border-radius: 5px; text-align: center; cursor: pointer; margin-top: 20px; text-decoration: none; }"
        + ".button:hover { background-color: #c0392b; }"
        + "</style></head><body>"
        + "<div class='container'>"
        + "<h1><span class='heart'>&hearts;</span> Thank You for Donating Blood!</h1>"
        + "<p>Dear <b>" + donation.getDonor().getFirstName() + " " + donation.getDonor().getLastName() + "</b>,</p>"
        + "<p>We are writing to express our sincere gratitude for your recent blood donation. Your generosity will make a real difference in the lives of others.</p>"
        + "<p>Here are the details of your donation:</p>"
        + "<ul class='blood-details'>"
        + "<li>Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM uuuu")) + "</li>"
        + "<li>Location: " + OrganisationLocation + "</li>"
        + "<li>Blood type: " + donation.getDonor().getBloodType() + "</li>"
        + "<li>Amount donated: " + donation.getQuantity() + " ml</li>"
        + "</ul>"
        + "<p>Your blood donation will be used to help patients in need of medical treatment. You have truly made a lifesaving contribution.</p>"
        + "<p>In the coming days, you will receive a notification regarding your blood test results. You can also access your donor profile and donation history on our website: "
        + "<p><a href='" + OrganisationWebsite + "' class='button'>Visit our website</a></p>"
        + "<p>Thank you again for your kindness and compassion. We encourage you to continue donating blood in the future.</p>"
        + "<p>Sincerely,<br/>" + OrganisationName + " Team</p>"
        + "</div>"
        + "</body></html>";
    sendEmail(donation.getDonor().getEmail(), subject, htmlContent);
  }

  @Async
  public void sendDonationUpdateNotification(Donation donation) {
    String subject = "Blood Donation Update Notification";
    String htmlContent = "<html><head><style>"
        + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }"
        + ".container { background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }"
        + "h1 { text-align: center; font-size: 24px; margin-bottom: 10px; color: #333; }"
        + "p { line-height: 1.5; color: #666; }"
        + ".heart { color: #e74c3c; font-size: 2em; margin-right: 5px; }"
        + ".blood-details { font-weight: bold; }"
        + "</style></head><body>"
        + "<div class='container'>"
        + "<h1><span class='heart'>&hearts;</span>Blood Donation Update Notification</h1>"
        + "<p>Dear " + donation.getDonor().getFirstName() + ",</p>"
        + "<p>We're writing to inform you that there has been an update to your recent blood donation.</p>"
        + "<p>Here are the details:</p>"
        + "<ul class='blood-details'>"
        + "<li>Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM uuuu")) + "</li>"
        + "<li>Location: " + OrganisationLocation + "</li>"
        + "<li>Blood type: " + donation.getDonor().getBloodType() + "</li>"
        + "<li>Updated Quantity: " + donation.getQuantity() + " ml</li>"
        + "<li>Updated Status: <b>" + donation.getDonationStatus() + "</b></li>"
        + "</ul>"
        + "<p>If you have any questions or concerns, please feel free to contact us at " + OrganisationPhone
        + " or reply to this email.</p>"
        + "<p>Thank you for your continued support!</p>"
        + "<p>Sincerely,<br/>" + OrganisationName + " Team</p>"
        + "</div>"
        + "</body></html>";
    sendEmail(donation.getDonor().getEmail(), subject, htmlContent);
  }

}
