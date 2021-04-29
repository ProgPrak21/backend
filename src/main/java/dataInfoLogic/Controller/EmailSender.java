package dataInfoLogic.Controller;

import dataInfoLogic.DataTypes.Content;
import dataInfoLogic.DataTypes.EmailAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailSender {


    @CrossOrigin
    @PostMapping(path = "/email")
    public ResponseEntity<?> SendMail(@RequestBody EmailAddress e) {

        sendEmailService(e);
        Content content = new Content();
        content.setContent("Maybe the e-Mail to "+e.getFirstName()+" has been sent successfully, maybe not");

        return ResponseEntity.ok(content);
    }


    @Autowired
    private JavaMailSender emailSender;

    //sending email function

    public void sendEmailService(EmailAddress e) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("blabla@idontknow.de");
        message.setReplyTo("blabla@idontknow.de");
        message.setTo(e.getEmailAddress());
        message.setSubject("We can send e-Mails");
        message.setText("Hello "+e.getFirstName()+","+
                "\n\nMajed and Kjell just proved, that they are able to send e-Mails."+
                "\n\nCheers"+
                "\nThe team without a team name so far");
        emailSender.send(message);
    }

}
