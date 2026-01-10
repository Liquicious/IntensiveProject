//package org.example.notificationservice.integration;
//
//import com.icegreen.greenmail.util.GreenMail;
//import com.icegreen.greenmail.util.ServerSetup;
//import org.example.notificationservice.service.EmailService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import jakarta.mail.internet.MimeMessage;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class EmailServiceIntegrationTest {
//
//    private GreenMail greenMail;
//
//    @Autowired
//    private EmailService emailService;
//
//    @BeforeEach
//    void setUp() {
//        greenMail = new GreenMail(new ServerSetup(3025, null, "smtp"));
//        greenMail.start();
//        greenMail.setUser("test@example.com", "test", "test");
//    }
//
//    @AfterEach
//    void tearDown() {
//        if (greenMail != null) {
//            greenMail.stop();
//        }
//    }
//
//    @Test
//    void sendEmail_ValidRequest_EmailSentSuccessfully() throws Exception {
//        String to = "recipient@example.com";
//        String subject = "Test Subject";
//        String text = "Test email content";
//
//        emailService.sendEmail(to, subject, text);
//
//        boolean emailReceived = greenMail.waitForIncomingEmail(5000, 1);
//        assertTrue(emailReceived, "Email should be received");
//
//        MimeMessage[] messages = greenMail.getReceivedMessages();
//        assertEquals(1, messages.length);
//
//        MimeMessage message = messages[0];
//        assertEquals(subject, message.getSubject());
//        assertEquals(to, message.getAllRecipients()[0].toString());
//        assertTrue(message.getContent().toString().contains(text));
//    }
//
//    @Test
//    void sendUserCreatedEmail_ValidData_SendsCreationEmail() throws Exception {
//        String email = "newuser@example.com";
//        String userName = "John Doe";
//
//        emailService.sendUserCreatedEmail(email, userName);
//
//        greenMail.waitForIncomingEmail(5000, 1);
//        MimeMessage[] messages = greenMail.getReceivedMessages();
//
//        assertEquals(1, messages.length);
//        assertEquals("Аккаунт успешно создан", messages[0].getSubject());
//        assertTrue(messages[0].getContent().toString()
//                .contains("Здравствуйте! Ваш аккаунт на сайте localhost:8080/api/users был успешно создан."));
//    }
//
//    @Test
//    void sendUserDeletedEmail_ValidData_SendsDeletionEmail() throws Exception {
//        String email = "deleteduser@example.com";
//        String userName = "Jane Smith";
//
//        emailService.sendUserDeletedEmail(email, userName);
//
//        greenMail.waitForIncomingEmail(5000, 1);
//        MimeMessage[] messages = greenMail.getReceivedMessages();
//
//        assertEquals(1, messages.length);
//        assertEquals("Аккаунт удален", messages[0].getSubject());
//        assertTrue(messages[0].getContent().toString()
//                .contains("Здравствуйте! Ваш аккаунт был удалён."));
//    }
//
//    @Test
//    void sendMultipleEmails_AllEmailsDelivered() throws Exception {
//        String[] emails = {"user1@example.com", "user2@example.com", "user3@example.com"};
//
//        for (String email : emails) {
//            emailService.sendEmail(email, "Test", "Content");
//        }
//
//        greenMail.waitForIncomingEmail(5000, 3);
//        MimeMessage[] messages = greenMail.getReceivedMessages();
//
//        assertEquals(3, messages.length);
//        assertEquals(emails[0], messages[0].getAllRecipients()[0].toString());
//        assertEquals(emails[1], messages[1].getAllRecipients()[0].toString());
//        assertEquals(emails[2], messages[2].getAllRecipients()[0].toString());
//    }
//}