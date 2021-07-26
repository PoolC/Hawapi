package com.moviePicker.api.auth;

import com.moviePicker.api.AcceptanceTest;
import com.moviePicker.api.domain.auth.dto.AuthorizationTokenRequest;
import com.moviePicker.api.domain.auth.dto.LoginRequest;
import com.moviePicker.api.domain.auth.dto.LoginResponse;
import com.moviePicker.api.domain.auth.dto.PasswordResetRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("memberDataLoader")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class AuthAcceptanceTest extends AcceptanceTest {
    static String authorizedEmail = "jasotn12@naver.com",
            unauthorizedEmail = "anfro2520@gmail.com",
            getAuthorizationTokenEmail = "authorizationCode@gmail.com",
            getPasswordResetTokenEmail = "passwordResetCode@gmail.com",
            notExistEmail = "notExist@gmail.com",
            password = "password123!", wrongPassword = "wrongPassword",
            resetPassword = "resetPassword123!";

    static String authorizationToken = "authorization_token", passwordResetToken = "password_reset_token";

    @Test
    @DisplayName("테스트 01: 로그인시 실패 401 (비밀번호가 틀렸을 때)")
    public void 로그인_실패_UNAUTHORIZED() {
        //given
        LoginRequest request = LoginRequest.builder()
                .loginId(unauthorizedEmail)
                .password(wrongPassword)
                .build();

        //when
        ExtractableResponse<Response> response = loginRequest(request);

        //then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("테스트 02: 로그인 실패 404 (해당 이메일을 가진 회원이 존재하지 않을 때)")
    public void 로그인_실패_NOT_FOUND() {
        //given
        LoginRequest request = LoginRequest.builder()
                .loginId(notExistEmail)
                .password(wrongPassword)
                .build();

        //when
        ExtractableResponse<Response> response = loginRequest(request);

        //then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @DisplayName("테스트 03: 로그인시 성공 200")
    public void 로그인_성공() {
        //given
        LoginRequest request = LoginRequest.builder()
                .loginId(unauthorizedEmail)
                .password(password)
                .build();

        //when
        ExtractableResponse<Response> response = loginRequest(request);

        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        String accessToken = response.as(LoginResponse.class).getAccessToken();
        assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("테스트 04: 이메일로 인증코드 보내기 실패 401 (로그인하지 않았을 때)")
    public void 이메일_인증코드_보내기_실패_UNAUTHORIZATION() {
        //given

        //when
        ExtractableResponse<Response> response = authorizationTokenSendRequest(null);

        //then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());

    }

    @Test
    @DisplayName("테스트 05: 이메일로 인증코드 보내기 실패 403 (이미 이메일 인증을 끝낸 상태일 때)")
    public void 이메일_인증코드_보내기_실패_FORBIDDEN() {
        //given
        String accessToken = authorizedLogin();

        //when
        ExtractableResponse<Response> response = authorizationTokenSendRequest(accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());

    }

    //TODO: 이메일이 실제로 왔는지 테스트하는 로직이 필요하다.
    @Test
    @DisplayName("테스트 06: 이메일로 인증코드 보내기 성공 200")
    public void 이메일_인증코드_보내기_성공() {
        //given
        String accessToken = unauthorizedLogin();

        //when
        ExtractableResponse<Response> response = authorizationTokenSendRequest(accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    @DisplayName("테스트 07: 이메일 인증코드 확인 실패 401 (로그인하지 않았을 때)")
    public void 이메일_인증코드_확인_실패_UNAUTHORIZED() {
        //given

        //when
        ExtractableResponse<Response> response = checkAuthorizationTokenRequest(null, null);

        //then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());

    }

    @Test
    @DisplayName("테스트 08: 이메일 인증코드 확인 실패 403 (이미 이메일 인증했을 때)")
    public void 이메일_인증코드_확인_실패_FORBIDDEN() {
        //given
        String accessToken = authorizedLogin();
        AuthorizationTokenRequest request = new AuthorizationTokenRequest(authorizationToken);

        //when
        ExtractableResponse<Response> response = checkAuthorizationTokenRequest(accessToken, request);

        //then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());

    }

    @Test
    @DisplayName("테스트 09: 이메일 인증코드 확인 실패 409 (인증코드 틀렸을시)")
    public void 이메일_인증코드_확인_실패_CONFLICT() {
        //given
        String accessToken = getAuthorizationTokenLogin();

        //when
        ExtractableResponse<Response> response = checkAuthorizationTokenRequest(accessToken, null);

        //then
        assertThat(response.statusCode()).isEqualTo(CONFLICT.value());

    }

    @Test
    @DisplayName("테스트 10: 이메일 인증코드 확인 성공 200")
    public void 이메일_인증코드_확인_성공() {
        //given
        String accessToken = getAuthorizationTokenLogin();
        AuthorizationTokenRequest request = new AuthorizationTokenRequest(authorizationToken);

        //when
        ExtractableResponse<Response> response = checkAuthorizationTokenRequest(accessToken, request);

        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());

    }

    @Test
    @DisplayName("테스트 11: 비밀번호 초기화 메일 보내기 실패 404 (해당 이메일을 가진 회원이 존재하지 않을 때)")
    public void 비밀번호_초기화_메일_보내기_실패_NOT_FOUND() {
        //given

        //when
        ExtractableResponse<Response> response = passwordResetTokenSendRequest(null);

        //then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }

    @Test
    @DisplayName("테스트 12: 비밀번호 초기화 메일 보내기 성공 200")
    public void 비밀번호_초기화_메일_보내기_성공() {
        //given

        //when
        ExtractableResponse<Response> response = passwordResetTokenSendRequest(authorizedEmail);

        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());

    }

    @Test
    @DisplayName("테스트 13: 비밀번호 변경 실패 400 (password가 없을 시)")
    public void 비밀번호_변경_실패_BAD_REQUEST_1() {
        //given
        PasswordResetRequest request = PasswordResetRequest.builder()
                .email(getPasswordResetTokenEmail)
                .password(null)
                .passwordCheck(null)
                .passwordRestToken(passwordResetToken)
                .build();

        //when
        ExtractableResponse<Response> response = passwordRestRequest(request);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }

    @Test
    @DisplayName("테스트 13: 비밀번호 변경 실패 400 (password와 password_check가 틀렸을 시)")
    public void 비밀번호_변경_실패_BAD_REQUEST_2() {
        //given
        PasswordResetRequest request = PasswordResetRequest.builder()
                .email(getPasswordResetTokenEmail)
                .password(resetPassword)
                .passwordCheck(wrongPassword)
                .passwordRestToken(passwordResetToken)
                .build();

        //when
        ExtractableResponse<Response> response = passwordRestRequest(request);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }

    @Test
    @DisplayName("테스트 15: 비밀번호 변경 실패 404 (해당 email을 가진 회원이 존재하지 않을 시)")
    public void 비밀번호_변경_실패_NOT_FOUND() {
        //given
        PasswordResetRequest request = PasswordResetRequest.builder()
                .email(notExistEmail)
                .password(resetPassword)
                .passwordCheck(resetPassword)
                .passwordRestToken(passwordResetToken)
                .build();

        //when
        ExtractableResponse<Response> response = passwordRestRequest(request);

        //then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }

    @Test
    @DisplayName("테스트 16: 비밀번호 변경 실패 409 (password_reset_token이 틀렸을 시)")
    public void 비밀번호_변경_실패_CONFLICT() {
        //given
        PasswordResetRequest request = PasswordResetRequest.builder()
                .email(getPasswordResetTokenEmail)
                .password(resetPassword)
                .passwordCheck(resetPassword)
                .passwordRestToken(null)
                .build();

        //when
        ExtractableResponse<Response> response = passwordRestRequest(request);

        //then
        assertThat(response.statusCode()).isEqualTo(CONFLICT.value());

    }

    @Test
    @DisplayName("테스트 17: 비밀번호 변경 성공")
    public void 비밀번호_변경_성공() {
        //given
        PasswordResetRequest request = PasswordResetRequest.builder()
                .email(getPasswordResetTokenEmail)
                .password(resetPassword)
                .passwordCheck(resetPassword)
                .passwordRestToken(passwordResetToken)
                .build();

        //when
        ExtractableResponse<Response> response = passwordRestRequest(request);

        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());

    }

    public static String unauthorizedLogin() {
        LoginRequest request = LoginRequest.builder()
                .loginId(unauthorizedEmail)
                .password(password)
                .build();

        return loginRequest(request)
                .as(LoginResponse.class)
                .getAccessToken();
    }

    public static String authorizedLogin() {
        LoginRequest request = LoginRequest.builder()
                .loginId(authorizedEmail)
                .password(password)
                .build();

        return loginRequest(request)
                .as(LoginResponse.class)
                .getAccessToken();
    }

    private String getAuthorizationTokenLogin() {
        LoginRequest request = LoginRequest.builder()
                .loginId(getAuthorizationTokenEmail)
                .password(password)
                .build();
        return loginRequest(request)
                .as(LoginResponse.class)
                .getAccessToken();
    }

    private static ExtractableResponse<Response> loginRequest(LoginRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(APPLICATION_JSON_VALUE)
                .when().post("/auth/login")
                .then().log().all()
                .extract();
    }


    private ExtractableResponse<Response> authorizationTokenSendRequest(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().post("/auth/authorization")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> checkAuthorizationTokenRequest(String accessToken, AuthorizationTokenRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(APPLICATION_JSON_VALUE)
                .when().put("/auth/authorization")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> passwordResetTokenSendRequest(String email) {
        return RestAssured
                .given().log().all()
                .body(email)
                .contentType(APPLICATION_JSON_VALUE)
                .when().post("/auth/password-reset")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> passwordRestRequest(PasswordResetRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(APPLICATION_JSON_VALUE)
                .when().put("/auth/password-reset")
                .then().log().all()
                .extract();
    }
}
