package com.moviePicker.api.comment;

import com.moviePicker.api.AcceptanceTest;
import com.moviePicker.api.auth.dto.LoginRequest;
import com.moviePicker.api.auth.dto.LoginResponse;
import com.moviePicker.api.comment.dto.CommentCreateRequest;
import com.moviePicker.api.comment.dto.CommentUpdateRequest;
import com.moviePicker.api.comment.repository.CommentRepository;
import com.moviePicker.api.review.dto.ReviewCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static com.moviePicker.api.auth.AuthAcceptanceTest.loginRequest;
import static com.moviePicker.api.movie.MovieAcceptanceDataLoader.memberEmail;
import static com.moviePicker.api.movie.MovieAcceptanceDataLoader.memberPassword;
import static com.moviePicker.api.review.ReviewAcceptanceDataLoader.commentIdOfAnotherMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@ActiveProfiles("reviewAcceptanceDataLoader")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CommentAcceptanceTest extends AcceptanceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("테스트 01: 댓글 작성 실패 403 : 로그인 안한 회원이 접근한 경우")
    public void 댓글_작성_실패_UNAUTHORIZED() throws Exception {
// given
        String accessToken = "";
        String content = "createCommentContent";
        CommentCreateRequest request = CommentCreateRequest.builder()
                .reviewId(1L)
                .content(content)
                .build();
        // when
        ExtractableResponse<Response> response = commentCreateRequest(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());
    }

    @Test
    @DisplayName("테스트 02: 댓글 작성 실패 404 : review id 가 잘못된 경우")
    public void 댓글_작성_실패_NOT_FOUND() throws Exception {
        // given
        String accessToken = defaultLogin();
        String content = "createCommentContent";
        CommentCreateRequest request = CommentCreateRequest.builder()
                .reviewId(-33L)
                .content(content)
                .build();
        // when
        ExtractableResponse<Response> response = commentCreateRequest(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }


    @Test
    @DisplayName("테스트 03: 댓글 작성 실패 400 :  내용을 입력하지 않았을때 ")
    public void 댓글_작성_실패_BAD_REQUEST() throws Exception {
        // given
        String accessToken = defaultLogin();
        String content = "";
        CommentCreateRequest request = CommentCreateRequest.builder()
                .reviewId(1L)
                .content(content)
                .build();
        // when
        ExtractableResponse<Response> response = commentCreateRequest(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }


    @Test
    @DisplayName("테스트 04: 댓글 작성 성공 200 ")
    public void 댓글_작성_성공_OK() throws Exception {

        // given
        String accessToken = defaultLogin();
        String content = "createCommentContent";
        CommentCreateRequest request = CommentCreateRequest.builder()
                .reviewId(1L)
                .content(content)
                .build();
        // when
        ExtractableResponse<Response> response = commentCreateRequest(accessToken, request);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(commentRepository.findCommentByContent(content).isPresent()).isEqualTo(true);
    }

    @Test
    @DisplayName("테스트 05: 댓글 수정하기 실패 403: 로그인 안한 회원이 접근한 경우 ")
    public void 댓글_수정_실패_FORBIDDEN() throws Exception {
        // given
        String accessToken = "";
        String content = "updateCommentContent";
        Long updatingCommentId = 65L;
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content(content)
                .build();
        // when
        ExtractableResponse<Response> response = commentUpdateRequest(accessToken, request,updatingCommentId);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());
    }

    @Test
    @DisplayName("테스트 06: 댓글 수정하기 실패 401 : 사용자가 작성한 댓글이 아닐때")
    public void 댓글_수정_실패_UNAUTHORIZED() throws Exception {
        // given
        String accessToken = defaultLogin();
        String content = "updateCommentContent";
        Long updatingCommentId = commentIdOfAnotherMember;
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content(content)
                .build();
        // when
        ExtractableResponse<Response> response = commentUpdateRequest(accessToken, request,updatingCommentId);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("테스트 07: 댓글 수정하기 실패 400 : 수정하는 내용이 없을때")
    public void 댓글_수정_실패_BAD_REQUEST() throws Exception {
        // given
        String accessToken = defaultLogin();
        String content = "";
        Long updatingCommentId = 65L;
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content(content)
                .build();
        // when
        ExtractableResponse<Response> response = commentUpdateRequest(accessToken, request,updatingCommentId);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    @DisplayName("테스트 08: 댓글 수정하기 실패 404: comment id 가 잘못된 경우")
    public void 댓글_수정_실패_NOT_FOUND() throws Exception {
        // given
        String accessToken = defaultLogin();
        String content = "updateCommentContent";
        Long updatingCommentId = -33L;
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content(content)
                .build();
        // when
        ExtractableResponse<Response> response = commentUpdateRequest(accessToken, request,updatingCommentId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @DisplayName("테스트 09: 댓글 수정하기 성공 200")
    public void 댓글_수정_성공_OK() throws Exception {
        // given
        String accessToken = defaultLogin();
        String content = "updateCommentContent";
        Long updatingCommentId = 65L;
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content(content)
                .build();
        // when
        ExtractableResponse<Response> response = commentUpdateRequest(accessToken, request,updatingCommentId);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(commentRepository.findCommentByContent(content).get().getId()).isEqualTo(65L);
    }

    @Test
    @DisplayName("테스트 10: 자신이 쓴 댓글 지우기 실패 403: 로그인 안한 회원이 접근한 경우 ")
    public void 댓글_지우기_실패_FORIBIDDEN() throws Exception {
        // given
        String accessToken = "";
        Long deletingCommentId = 65L;
        // when
        ExtractableResponse<Response> response = commentDeleteRequest(accessToken, deletingCommentId);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());
    }

    @Test
    @DisplayName("테스트 11: 자신이 쓴 댓글 지우기 실패 401: 사용자가 작성한 댓글이 아닐때 ")
    public void 댓글_지우기_실패_UNAUTHORIZED() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long deletingCommentId = commentIdOfAnotherMember;
        // when
        ExtractableResponse<Response> response = commentDeleteRequest(accessToken, deletingCommentId);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("테스트 12: 자신이 쓴 댓글 지우기 실패 404: comment id 가 잘못된 경우")
    public void 댓글_지우기_실패_NOT_FOUND() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long deletingCommentId = -33L;
        // when
        ExtractableResponse<Response> response = commentDeleteRequest(accessToken, deletingCommentId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @DisplayName("테스트 13: 자신이 쓴 댓글 지우기 성공 200")
    public void 댓글_지우기_성공_OK() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long deletingCommentId = 65L;
        // when
        ExtractableResponse<Response> response = commentDeleteRequest(accessToken, deletingCommentId);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(commentRepository.findById(deletingCommentId).isPresent()).isEqualTo(false);
    }

    public String defaultLogin() {
        LoginRequest request = LoginRequest.builder()
                .email(memberEmail)
                .password(memberPassword)
                .build();

        return loginRequest(request)
                .as(LoginResponse.class)
                .getAccessToken();
    }


    private static ExtractableResponse<Response> commentCreateRequest(String accessToken, CommentCreateRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(APPLICATION_JSON_VALUE)
                .when().post("/comments")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> commentUpdateRequest(String accessToken, CommentUpdateRequest request, Long commentId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(APPLICATION_JSON_VALUE)
                .when().put("/comments/{commentId}", commentId)
                .then().log().all()
                .extract();
    }


    private static ExtractableResponse<Response> commentDeleteRequest(String accessToken, Long commentId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .when().delete("/comments/{commentId}", commentId)
                .then().log().all()
                .extract();
    }


}
