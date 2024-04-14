# Blood Donation RestFul API With Email Integration

- Developed A Robust Restful API For Blood Donation Management.
- Having Main & Spring-Security Branches
- Implemented Email Notification Interation
- Implemented A Comprehensive System For Tracking Donor Information, Appointments, And Medical History.
- Utilized Java And Relevant Frameworks To Build The API, Ensuring Scalability And Performance.
- Designed Entities/Models For Donor, BloodBank, Staff, Appointment, MedicalHistory, And Donation.
- Incorporated Features Such As Donor Registration, Appointment Scheduling, And Medical History Recording.
- Established Relationships Between Entities To Maintain Data Integrity And Consistency.
- Implemented Security Measures To Protect Sensitive Information And Ensure Data Privacy.
- Created Endpoints For CRUD Operations On Various Entities, Providing A User-Friendly Interface For Interactions.
- Ensured Proper Error Handling And Response Messages For A Smooth User Experience.
- Optimized Database Interactions For Efficient Data Retrieval And Storage.
- Conducted Thorough Testing To Validate Functionality, Security, And Overall System Stability.
- Documented The API For Easy Integration And Future Maintenance.
- Addressed Challenges With Innovative Problem-Solving And Continuous Improvement.
- Excited To Contribute To The Healthcare Ecosystem With The Blood Donation API? Download This Project And Start Running Setup .

## Variables

    bloodDonation.organisation.Id=${ORG_ID}
    bloodDonation.organisation.name=${ORG_NAME}
    bloodDonation.organisation.website=https://sayeedthedev.web.app
    bloodDonation.organisation.PhoneNumber=${NUMBER}
    bloodDonation.organisation.Location=${LOCATION}
    bloodDonation.organisation.OrganisationEmail=${EMAIL}
    bloodDonation.organisation.OrganisationEmailPassword=${PASSWORD}

    # DataSource Configuration
    spring.datasource.url=jdbc:mysql://${PROD_DB_HOST}:${PROD_DB_PORT}/${PROD_DB_NAME}
    spring.datasource.username=${PROD_DB_USERNAME}
    spring.datasource.password=${PROD_DB_PASSWORD}
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

## Swagger API URL `http://localhost:8080/swagger-ui.html`

## POSITION OF STAFF

- Appoint: Manages donor appointments, schedules, and confirms donation appointments.

- Donor: Responsible for collecting and maintaining donor medical histories, including any relevant conditions, medications, and allergies.

- Phlebotomist/Nurse: Draws blood from donors safely and efficiently during donation events.

- Manager: Oversees the overall operations of the blood donation camp or organization, ensuring smooth coordination among staff, volunteers, and donors, and handling any issues that may arise.

# Docs Of End Points

## Donor

- Base URL: `/api/v1/donor`

  - **POST** `createDonor`

    - @Require Donor
    - @Return HttpStatus.Code

  - **GET** `showDonor`

    - @Return HttpStatus with Donor

  - **GET** `/{donorId}`

    - @Param `donorId`: Integer, the unique identifier of the donor
    - @Return HttpStatus

  - **DELETE** `/{donorId}`

    - @Param `donorId`: Integer, the unique identifier of the donor
    - @Return HttpStatus

  - **PATCH** `updateDonor`
    - @Require Donor
    - @Return HttpStatus

## AppointController

- Base URL: `/api/v1/appointment`

  - **POST** `createAppointment`

    - @Require Appointment, Integer donorId
    - @Return HttpStatus

  - **GET** `showAppointment`

    - @Return HttpStatus With Appointment

  - **GET** `/{appointmentId}`

    - @Param `appointmentId`: Integer, the unique identifier of the appointment
    - @Return HttpStatus With Appointment

  - **DELETE** `/{appointmentId}`

    - @Param `appointmentId`: Integer, the unique identifier of the appointment
    - @Return HttpStatus

  - **PATCH** `updateAppointment`

    - @Require Appointment, Integer id
    - @Return HttpStatus

  - **GET** `todayAppointments`

    - @Return List of Todays Appointment

  - **GET** `doAppointDonor`

    - @Return List of Donors Who are Not Appointed Yet

  - **GET** `findByDate`

    - @Require date (LocalDate)
    - @Return List of Appointments

## MedicalHistController

- Base URL: `/api/v1/medicalHistory`

  - **POST** `createHistory`

    - @Require MedicalHistory, Integer donorId
    - @Return HttpStatus

  - **GET** `showHistory`

    - @Return HttpStatus With MedicalHistory

  - **GET** `/{historyId}`

    - @Param `historyId`: Integer, the unique identifier of the medical history
    - @Return HttpStatus

  - **GET** `findByDonor/{donorId}`

    - @Param `donorId`: Integer, the unique identifier of the donor
    - @Return HttpStatus

  - **DELETE** `/{historyId}`

    - @Param `historyId`: Integer, the unique identifier of the medical history
    - @Return HttpStatus

  - **PATCH** `updateHistory`
    - @Require MedicalHistory
    - @Return HttpStatus

## StaffController

- Base URL: `/api/v1/Staff`

  - **POST** `createStaff`

    - @Require Staff
    - @Return HttpStatus

  - **GET** `showStaff`

    - @Return HttpStatus With Staff

  - **GET** `/{staffId}`

    - @Param `staffId`: Integer, the unique identifier of the staff
    - @Return HttpStatus

  - **DELETE** `/{staffId}`

    - @Param `staffId`: Integer, the unique identifier of the staff
    - @Return HttpStatus

  - **PATCH** `updateStaff`

    - @Require Staff
    - @Return HttpStatus

  - **PATCH** `updateStaffPosition`
    - @Require Staff
    - @Return HttpStatus

## DonationController

- Base URL: `/api/v1/Donation`

  - **POST** `createDonation`

    - @Require Donation, Integer appointId
    - @Return HttpStatus

  - **GET** `showDonation`

    - @Return HttpStatus With Donation

  - **GET** `/{donationId}`

    - @Param `donationId`: Integer, the unique identifier of the donation
    - @Return HttpStatus With Donation

  - **PATCH** `updateDonation`
    - @Require Donation, Integer donationId
    - @Return HttpStatus

## BloodBankController

- Base URL: `/api/v1/BloodBank`

  - **POST** `createBank`

    - @Require BloodBank, Integer bloodBankId
    - @Return HttpStatus

  - **GET** `showBank`

    - @Return HttpStatus With BloodBank

  - **GET** `/{bloodBankId}`

    - @Param `bloodBankId`: Integer, the unique identifier of the BloodBank
    - @Return HttpStatus With BloodBank

  - **PATCH** `updateBloodBank`
    - @Require BloodBank, Integer bloodBankId
    - @Return HttpStatus
