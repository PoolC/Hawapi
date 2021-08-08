package com.moviePicker.api.member;


import com.moviePicker.api.AcceptanceTest;
import com.moviePicker.api.member.dto.MemberCreateRequest;
import com.moviePicker.api.member.dto.MemberUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import javax.print.attribute.standard.Media;

import static com.moviePicker.api.auth.AuthAcceptanceTest.authorizedLogin;
import static com.moviePicker.api.auth.AuthAcceptanceTest.unauthorizedLogin;
import static com.moviePicker.api.member.MemberDataLoader.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@ActiveProfiles("memberDataLoader")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class MemberAcceptanceTest extends AcceptanceTest {

    public static String

            name = "moviePicker", emptyName = "",
            nickname = "딸기성하", emptyNickName = "", wrongFormatNickname = "asdf", new_nickname="새로운닉네임",
            password = "password123!", emptyPassword = "", wrongPassword = "wrongPassword", wrongFormatPassword = "aaa11",
            passwordCheck = "password123!", wrongPasswordCheck = "asdfsd23@",
            email = "new_email@gmail.com", emptyEmail = "", wrongFormatEmail = "strawberrySungha.com@naver";


    @Test
    @DisplayName("테스트 01: 회원가입 실패 400: 하나의 컬럼이라도 없을 경우")
    public void 회원가입_실패_BAD_REQUEST_1() throws Exception {

        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .nickname(nickname)
                .name(emptyName)
                .password(password)
                .passwordCheck(passwordCheck)
                .email(email)
                .build();

        // when
        ExtractableResponse<Response> response = memberCreateRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }

    @Test
    @DisplayName("테스트 02: 회원가입 실패 400: 패스워드가 맞지 않을 때")
    public void 회원가입_실패_BAD_REQUEST_2() throws Exception {

        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .nickname(nickname)
                .name(name)
                .password(password)
                .passwordCheck(wrongPasswordCheck)
                .email(email)

                .build();

        // when
        ExtractableResponse<Response> response = memberCreateRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    @DisplayName("테스트 03: 회원가입 실패 400: 잘못된 형식으로 보낼때")
    public void 회원가입_실패_BAD_REQUEST_3() {

        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .nickname(nickname)
                .name(name)
                .password(password)
                .passwordCheck(passwordCheck)
                .email(wrongFormatEmail)
                .build();

        // when
        ExtractableResponse<Response> response = memberCreateRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }


    @Test
    @DisplayName("테스트 04: 회원가입 실패 409: 중복된 닉네임")
    public void 회원가입_실패_CONFLICT_1() {
        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .nickname(authorizedNickname)
                .name(name)
                .password(password)
                .passwordCheck(passwordCheck)
                .email(email)
                .build();

        // when
        ExtractableResponse<Response> response = memberCreateRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
    }

    @Test
    @DisplayName("테스트 05: 회원가입 실패 409: 중복된 이메일")
    public void 회원가입_실패_CONFLICT_2() {
        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .nickname(nickname)
                .name(name)
                .password(password)
                .passwordCheck(passwordCheck)
                .email(authorizedEmail)
                .build();

        // when
        ExtractableResponse<Response> response = memberCreateRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(CONFLICT.value());

    }


    @Test
    @DisplayName("테스트 07: 회원가입 성공 200")
    public void 회원가입_성공_OK() {
        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .nickname(nickname)
                .name(name)
                .password(password)
                .passwordCheck(passwordCheck)
                .email(email)
                .build();

        // when
        ExtractableResponse<Response> response = memberCreateRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    @DisplayName("테스트 08: 회원정보 수정 실패 403 : 로그인 안 할 시")
    public void 회원정보_수정_실패_NOT_FOUND_1() {
        // given
        String accessToken = "";
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .nickname(nickname)
                .password(password)
                .passwordCheck(passwordCheck)
                .build();

        // when
        ExtractableResponse<Response> response = memberUpdateRequest(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }


    @Test
    @DisplayName("테스트 09: 회원정보 수정 실패 400 ; 잘못된 형식으로 보낼 때")
    public void 회원정보_수정_실패_BAD_REQUEST_1() {
        // given
        String accessToken = authorizedLogin();
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .nickname(nickname)
                .password(wrongFormatPassword)
                .passwordCheck(passwordCheck)
                .build();

        // when
        ExtractableResponse<Response> response = memberUpdateRequest(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }


    @Test
    @DisplayName("테스트 10: 회원정보 수정 실패 409 : 중복된 닉네임이 있을 경우")
    public void 회원정보_수정_실패_CONFLICT_1() {
        // given
        String accessToken = authorizedLogin();
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .nickname(authorizedNickname)
                .password(password)
                .passwordCheck(passwordCheck)
                .build();

        // when
        ExtractableResponse<Response> response = memberUpdateRequest(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
    }

    @Test
    @DisplayName("테스트 12: 회원정보 수정 성공 200")
    public void 회원정보_수정_성공_OK() {
        // given
        String accessToken = authorizedLogin();
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .nickname(new_nickname)
                .password(password)
                .passwordCheck(passwordCheck)
                .build();

        // when
        ExtractableResponse<Response> response = memberUpdateRequest(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    @DisplayName("테스트 13: 회원 탈퇴 실패 403 : 본인이 아닐 시")
    public void 회원_탈퇴_실패_FORBIDDEN_1() throws Exception {
        // given
        String accessToken = unauthorizedLogin();

        // when
        ExtractableResponse<Response> response = memberWithdrawalRequest(accessToken, authorizedNickname);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());

    }


    @Test
    @DisplayName("테스트 15: 회원 탈퇴 실패  400  : 잘못된 닉네임일때")
    public void 회원_탈퇴_실패_BAD_REQUEST_1() throws Exception {
        // given
        String accessToken = authorizedLogin();
        // when
        ExtractableResponse<Response> response = memberWithdrawalRequest(accessToken, wrongFormatNickname);
        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }

    @Test
    @DisplayName("테스트 16: 회원탈퇴 성공  200 ")
    public void 회원_탈퇴_성공_OK() throws Exception {
        // given
        String accessToken = authorizedLogin();
        // when
        ExtractableResponse<Response> response = memberWithdrawalRequest(accessToken, authorizedNickname);
        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());

    }

    @Test
    @DisplayName("테스트 17: 이메일 중복체크")
    public void 이메일_중복체크_성고() throws Exception{
        //given
        String existing_email="anfro2520@gmail.com";
        //when
        ExtractableResponse<Response> response = sameEmailRequest(existing_email);
        //then
        assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
    }

    @Test
    @DisplayName("테스트 18: 닉네임 중복체크")
    public void 닉네임_중복체크_성고() throws Exception{
        //given
        String existing_nickname="anfro2520@gmail.com";
        //when
        ExtractableResponse<Response> response = sameNicknameRequest(existing_nickname);
        //then
        assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
    }

    private static ExtractableResponse<Response> sameEmailRequest(String email){
        return RestAssured
                .given().log().all()
                .when().get("/members/emails/{email}",email)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> sameNicknameRequest(String nickname){
        return RestAssured
                .given().log().all()
                .when().get("/members/nicknames/{nickname}",nickname)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> memberCreateRequest(MemberCreateRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(APPLICATION_JSON_VALUE)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> memberUpdateRequest(String accessToken, MemberUpdateRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(APPLICATION_JSON_VALUE)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> memberWithdrawalRequest(String accessToken, String nickname) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().put("/members/{nickname}",nickname)
                .then().log().all()
                .extract();
    }


}
