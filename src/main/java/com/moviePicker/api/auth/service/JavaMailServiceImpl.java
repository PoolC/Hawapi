package com.moviePicker.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class JavaMailServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    //TODO: 환경변수로 바꾸기
    String address = "https://moviePicker.com";

    @Override
    public void sendEmailAuthorizationToken(String email, String authorizationToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setSubject("무비피커 아이디 인증 안내메일입니다.");
        helper.setText(String.format("안녕하세요. 무비피커입니다. 아이디를 인증하시려면 해당 url( %s )로 들어가시면 됩니다.\n" +
                        "감사합니다.",
                address + "/auth/authorization?authorizationToken=" + authorizationToken));
        helper.setTo(email);
        mailSender.send(message);
    }

    @Override
    public void sendEmailPasswordResetToken(String email, String resetPasswordToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setSubject("무비피커 아이디 비밀번호 재설정 안내메일입니다.");
        helper.setText(String.format("안녕하세요 무비피커입니다. 비밀번호 재설정을 하려면 해당 url( %s )로 접속하여 변경하시면 됩니다.\n" +
                        "감사합니다.",
                address + "/auth/password-reset?passwordResetToken=" + resetPasswordToken));
        helper.setTo(email);
        mailSender.send(message);
    }
}
