package com.moviePicker.api.member;


import com.moviePicker.api.auth.AuthAcceptanceTest;
import com.moviePicker.api.auth.dto.LoginRequest;
import com.moviePicker.api.common.exception.NotSameException;
import com.moviePicker.api.member.dto.MemberCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;


import java.util.Optional;

import static com.moviePicker.api.member.MemberDataLoader.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@ActiveProfiles("memberDataLoader")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class MemberAcceptanceTest {

    public static String

    name="moviePicker", emptyName="", nullName= null,
    nickname="딸기성하", emptyNickName= "",nullNickName= null, wrongFormatNickname="asdf",
    password = "password123!", emptyPassword="", nullPassword=null, wrongPassword = "wrongPassword", wrongFormatPassword="aaa11",
    passwordCheck="password123!", nullPasswordCheck=null, wrongPasswordCheck = "asdfsd23@",
    email = "anfro2520@gmail.com", emptyEmail="", nullEmail = null, wrongFormatEmail="strawberrySungha.com@naver";


    @Test
    @DisplayName("테스트 01: 회원가입 실패 400: 하나의 컬럼이라도 없을 경우")
    public void 회원가입_실패_BAD_REQUEST_1() throws Exception{

        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .nickname(nickname)
                .name(name)
                .password(password)
                .passwordCheck(nullPasswordCheck)
                .email(email)
                .build();

        // when
        ExtractableResponse<Response> response = memberCreateRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }

    @Test
    @DisplayName("테스트 02: 회원가입 실패 400: 패스워드가 맞지 않을 때")
    public void 회원가입_실패_BAD_REQUEST_2() throws Exception{
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
    public void 회원가입_실패_BAD_REQUEST_3(){

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
    public void 회원가입_실패_CONFLICT_1(){
        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .nickname(existingNickName)
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
    public void 회원가입_실패_CONFLICT_2(){
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
    public void 회원가입_성공_OK(){
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
    @DisplayName("테스트 08: 회원정보 수정 실패 400 ; 잘못된 형식으로 보낼 때")
    public void 회원정보_수정_실패_BAD_REQUEST_1(){

    }


    @Test
    @DisplayName("테스트 09: 회원정보 수정 실패 403 : 로그인 안 할 시")
    public void 회원정보_수정_실패_NOT_FOUND_1(){}

    @Test
    @DisplayName("테스트 10: 회원정보 수정 실패 409 : 중복된 이메일이 있을 경우")
    public void 회원정보_수정_실패_CONFLICT_1(){}

    @Test
    @DisplayName("테스트 11: 회원정보 수정 실패 409 : 중복된 닉네임이 있을 경우")
    public void 회원정보_수정_실패_CONFLICT_2(){}

    @Test
    @DisplayName("테스트 12: 회원정보 수정 성공 200")
    public void 회원정보_수정_실패_OK(){}

    @Test
    @DisplayName("테스트 13: 회원정보 삭제 실패 403 : 본인이 아닐 시")
    public void 회원정보_삭제_실패_FORBIDDEN_1() throws Exception{
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 14: 회원정보 삭제 실패 403 : 관리자가 아닐 시")
    public void 회원정보_삭제_실패_FORBIDDEN_2() throws Exception{
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 15: 회원정보 삭제 실패  400  : 잘못된 닉네임일때")
    public void 회원정보_삭제_실패_BAD_REQUEST_1() throws Exception{
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 16: 회원정보 삭제 성공  200 ")
    public void 회원정보_삭제_성공() throws Exception{
        // given

        // when

        // then

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





}
