package com.moviePicker.api.domain.auth.service;

public interface MailService {
    public void sendEmailAuthorizationToken(String email, String authorizationToken) throws Exception;

    public void sendEmailPasswordResetToken(String email, String resetPasswordToken) throws Exception;
}
