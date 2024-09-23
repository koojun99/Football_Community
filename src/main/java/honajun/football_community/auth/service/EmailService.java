package honajun.football_community.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String code) {
        // 이메일 전송 로직
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("koojun99@naver.com");
        message.setTo(email);
        message.setSubject("ShooToday 이메일 인증 코드");
        message.setText("인증 코드는 다음과 같습니다: " + code);

        mailSender.send(message);
    }
}
