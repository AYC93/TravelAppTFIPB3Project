package travel.app.service.RepoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String toEmail, String message){
        SimpleMailMessage mailMsg = new SimpleMailMessage();
        mailMsg.setTo(toEmail);
        mailMsg.setSubject("Planned Item");
        mailMsg.setText(message);
        mailMsg.setFrom(fromEmail);
        mailSender.send(mailMsg);
        System.out.println("email sent!");
    }
}
