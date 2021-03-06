package com.moviePicker.api.review;

import com.moviePicker.api.AcceptanceTest;
import com.moviePicker.api.auth.dto.LoginRequest;
import com.moviePicker.api.auth.dto.LoginResponse;
import com.moviePicker.api.comment.repository.CommentRepository;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.dto.ReviewCreateRequest;
import com.moviePicker.api.review.dto.ReviewResponse;
import com.moviePicker.api.review.dto.ReviewUpdateRequest;
import com.moviePicker.api.review.dto.ReviewsResponse;
import com.moviePicker.api.review.repository.ReviewRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;

import java.util.Optional;

import static com.moviePicker.api.auth.AuthAcceptanceTest.loginRequest;


import static com.moviePicker.api.review.ReviewAcceptanceDataLoader.*;
import static com.moviePicker.api.review.ReviewAcceptanceDataLoader.memberEmail;
import static com.moviePicker.api.review.ReviewAcceptanceDataLoader.memberNickname;
import static com.moviePicker.api.review.ReviewAcceptanceDataLoader.memberPassword;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("reviewAcceptanceDataLoader")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class ReviewAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("????????? 01: ?????? ?????? ?????? 403: ??????????????? ????????????")
    public void ??????_??????_??????_FORBIDDEN() throws Exception {
        // given
        String accessToken="";
        String movieId=specificMovieCode;
        String title="createReviewTitle";
        String content="createReviewContent";
        ReviewCreateRequest request=ReviewCreateRequest.builder()
                .movieId(movieId)
                .title(title)
                .content(content)
                .build();

        // when
        ExtractableResponse<Response> response = reviewCreateRequest(accessToken,request);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());
    }


    @Test
    @DisplayName("????????? 02: ?????? ?????? ?????? 400: title/content ??? ????????? ??????")
    public void ??????_??????_??????_BAD_REQUEST() throws Exception {
        // given
        String accessToken=defaultLogin();
        String movieId=specificMovieCode;
        String title="";
        String content="createReviewContent";
        ReviewCreateRequest request=ReviewCreateRequest.builder()
                .movieId(movieId)
                .title(title)
                .content(content)
                .build();

        // when
        ExtractableResponse<Response> response = reviewCreateRequest(accessToken,request);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    @DisplayName("????????? 03: ?????? ?????? ?????? 404: ????????? movie id ??? ??????")
    public void ??????_??????_??????_NOT_FOUND() throws Exception {
        // given
        String accessToken=defaultLogin();
        String movieId="notExistingMovieId";
        String title="createReviewTitle";
        String content="createReviewContent";
        ReviewCreateRequest request=ReviewCreateRequest.builder()
                .movieId(movieId)
                .title(title)
                .content(content)
                .build();

        // when
        ExtractableResponse<Response> response = reviewCreateRequest(accessToken,request);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @DisplayName("????????? 04: ?????? ?????? ?????? 200")
    public void ??????_??????_??????_OK() throws Exception {
        // given
        String accessToken=defaultLogin();
        String movieId=specificMovieCode;
        String title="createReviewTitle";
        String content="createReviewContent";
        ReviewCreateRequest request=ReviewCreateRequest.builder()
                .movieId(movieId)
                .title(title)
                .content(content)
                .build();

        // when
        ExtractableResponse<Response> response = reviewCreateRequest(accessToken,request);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(reviewRepository.findByTitle(title).isPresent()).isEqualTo(true);
    }

    @Test
    @DisplayName("????????? 05: ?????? ???????????? ?????? 403: ????????? ?????? ????????? ????????? ?????? ")
    public void ??????_??????_??????_FORBIDDEN() throws Exception {
        // given
        String accessToken="";
        Long reviewId=1L;
        String title="updateReviewTitle";
        String content="updateReviewContent";
        ReviewUpdateRequest request=ReviewUpdateRequest.builder()
                .title(title)
                .content(content)
                .build();

        // when
        ExtractableResponse<Response> response = reviewUpdateRequest(accessToken,request,reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());
    }

    @Test
    @DisplayName("????????? 06: ?????? ???????????? ?????? 401: ???????????? ????????? ????????? ????????? ")
    public void ??????_??????_??????_UNAUTHORIZED() throws Exception {
        // given
        String accessToken=defaultLogin();
        Long reviewId=64L;
        String title="updateReviewTitle";
        String content="updateReviewContent";
        ReviewUpdateRequest request=ReviewUpdateRequest.builder()
                .title(title)
                .content(content)
                .build();

        // when
        ExtractableResponse<Response> response = reviewUpdateRequest(accessToken,request,reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("????????? 07: ?????? ???????????? ?????? 404: review id ??? ????????? ??????")
    public void ??????_??????_??????_NOT_FOUND() throws Exception {
        // given
        String accessToken=defaultLogin();
        Long reviewId=100L;
        String title="updateReviewTitle";
        String content="updateReviewContent";
        ReviewUpdateRequest request=ReviewUpdateRequest.builder()
                .title(title)
                .content(content)
                .build();

        // when
        ExtractableResponse<Response> response = reviewUpdateRequest(accessToken,request,reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @Transactional
    @DisplayName("????????? 08: ?????? ???????????? ?????? 200")
    public void ??????_??????_??????_OK() throws Exception {
        // given
        String accessToken=defaultLogin();
        Long reviewId=1L;
        String title="updateReviewTitle";
        String content="updateReviewContent";
        ReviewUpdateRequest request=ReviewUpdateRequest.builder()
                .title(title)
                .content(content)
                .build();

        // when
        ExtractableResponse<Response> response = reviewUpdateRequest(accessToken,request,reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        Review review=reviewRepository.findByTitle("updateReviewTitle").orElseThrow();
        assertThat(review.getMember().getEmail()).isEqualTo(memberEmail);

    }

    @Test
    @DisplayName("????????? 09: ?????? ????????? ?????? 403: ????????? ?????? ????????? ????????? ?????? ")
    public void ??????_?????????_??????_FORBIDDEN() throws Exception {
        // given
        String accessToken="";
        Long reviewId=1L;

        // when
        ExtractableResponse<Response> response = reviewDeleteRequest(accessToken,reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());
    }

    @Test
    @DisplayName("????????? 10:  ?????? ????????? ?????? 401: ???????????? ????????? ????????? ????????? ")
    public void ??????_?????????_??????_UNAUTHORIZED() throws Exception {
        // given
        String accessToken=defaultLogin();
        Long reviewId=64L;

        // when
        ExtractableResponse<Response> response = reviewDeleteRequest(accessToken,reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("????????? 11: ?????? ????????? ?????? 404: review id ??? ????????? ??????")
    public void ??????_?????????_??????_NOT_FOUND() throws Exception {
        // given
        String accessToken=defaultLogin();
        Long reviewId=100L;

        // when
        ExtractableResponse<Response> response = reviewDeleteRequest(accessToken,reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @DisplayName("????????? 12: ?????? ????????? ?????? 200")
    public void ??????_?????????_??????_OK() throws Exception {
        // given
        String accessToken=defaultLogin();
        Long reviewId=4L;

        // when
        ExtractableResponse<Response> response = reviewDeleteRequest(accessToken,reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(reviewRepository.findById(reviewId)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("????????? 13: ?????????????????? ?????? ?????? ?????? 404: ?????? ID ??? ????????? ??????")
    public void ??????????????????_????????????_??????_NOT_FOUND() throws Exception {
        // given
        Long reviewId=100L;

        // when
        ExtractableResponse<Response> response = reviewFindByReviewId(reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }


    @Test
    @DisplayName("????????? 14: ?????????????????? ?????? ?????? ?????? 200")
    public void ??????????????????_????????????_??????_OK() throws Exception {
        // given
        Long reviewId=1L;

        // when
        ExtractableResponse<Response> response = reviewFindByReviewId(reviewId);
        ReviewResponse responseBody = response.as(ReviewResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responseBody.getMovieCode()).isEqualTo("movie0");
    }


    @Test
    @DisplayName("????????? 15: ?????????????????? ?????? ?????? ?????? 404: ????????? movie id??? ??????")
    public void ?????????_????????????_??????_NOT_FOUND() throws Exception {

        // given
        String movieId = "wrongMovieCode";

        // when
        ExtractableResponse<Response> response = searchReviewByMovieCode(movieId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @DisplayName("????????? 16: ?????????????????? ???????????? ?????? ?????? 200")
    public void ?????????_????????????_??????_OK() throws Exception {

        // given
        String movieId = specificMovieCode;

        // when
        ExtractableResponse<Response> response = searchReviewByMovieCode(movieId);
        ReviewsResponse responseBody = response.as(ReviewsResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responseBody.getReviews().get(0).getMovieCode().equals(movieId));
    }


    @Test
    @DisplayName("????????? 17: ?????????????????? ?????? ??? ?????? ???????????? ?????? 403: ????????? ?????? ????????? ????????? ??????")
    public void ?????????_?????????_??????_????????????_??????_FORBIDDEN() throws Exception {

        // given
        String accessToken = "";
        String movieId = specificMovieCode;

        // when
        ExtractableResponse<Response> response = searchMyReviewByMovieCode(accessToken, movieId);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());
    }


    @Test
    @DisplayName("????????? 18: ?????????????????? ?????? ??? ?????? ???????????? ?????? 404: movie id??? ?????? ????????????")
    public void ?????????_?????????_??????_????????????_??????_NOT_FOUND() throws Exception {

        // given
        String accessToken = defaultLogin();
        String movieId = "wrongMovieCode";

        // when
        ExtractableResponse<Response> response = searchMyReviewByMovieCode(accessToken, movieId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @DisplayName("????????? 19: ?????????????????? ?????? ??? ?????? ???????????? ?????? 200")
    public void ?????????_?????????_??????_????????????_??????_OK() throws Exception {

        // given
        String accessToken = defaultLogin();
        String movieId = specificMovieCode;

        // when
        ExtractableResponse<Response> response = searchMyReviewByMovieCode(accessToken, movieId);
        ReviewsResponse responseBody = response.as(ReviewsResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responseBody.getReviews().get(0).getNickname().equals(memberNickname));
    }


    @Test
    @DisplayName("????????? 20: ?????? ?????? ??? ?????? ?????? ?????? 403: ????????? ?????? ????????? ????????? ?????? ")
    public void ??????_????????????_??????_FORBIDDEN() throws Exception {

        // given
        String accessToken = "";
        Long reviewId = 1L;

        // when
        ExtractableResponse<Response> response = recommendReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());
    }

    @Test
    @DisplayName("????????? 21: ?????? ?????? ??? ?????? ?????? ?????? 404: review id??? ????????? ?????? ")
    public void ??????_????????????_??????_NOT_FOUND() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long reviewId = -33L;

        // when
        ExtractableResponse<Response> response = recommendReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @Transactional
    @DisplayName("????????? 22: ?????? ?????? ?????? ?????? 200 ")
    public void ??????_??????_??????_??????_OK() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long reviewId = 1L;

        // when
        ExtractableResponse<Response> response = recommendReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(reviewRepository.findById(reviewId).get().getRecommendationCount().equals(0));
        assertThat(reviewRepository.findById(reviewId).get().getRecommendationList().size()).isEqualTo(0);

    }

    @Test
    @Transactional
    @DisplayName("????????? 23: ?????? ?????? ?????? 200 ")
    public void ??????_??????_??????_OK() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long reviewId = 1L;

        // when
        ExtractableResponse<Response> response = recommendReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(reviewRepository.findById(reviewId).get().getRecommendationCount().equals(1));
        assertThat(reviewRepository.findById(reviewId).get().getRecommendationList().size()).isEqualTo(1);
    }



    @Test
    @DisplayName("????????? 24: ?????? ?????? ?????? 403: ????????? ?????? ????????? ????????? ?????? ")
    public void ??????_??????_??????_FORBIDDEN() throws Exception {

        // given
        String accessToken = "";
        Long reviewId = 1L;

        // when
        ExtractableResponse<Response> response = reportReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());

    }

    @Test
    @DisplayName("????????? 25: ?????? ?????? ?????? 404: review id??? ????????? ?????? ")
    public void ??????_??????_??????_NOT_FOUND() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long reviewId = -33L;

        // when
        ExtractableResponse<Response> response = reportReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }

    @Test
    @DisplayName("????????? 26: ?????? ?????? ?????? 200 ")
    public void ??????_??????_??????_OK() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long reviewId = 1L;

        // when
        ExtractableResponse<Response> response = reportReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(reviewRepository.findById(reviewId).get().getReportCount().equals(1));

    }

    @Test
    @DisplayName("????????? 27: ?????? ?????? ?????? 400: ?????? ?????? ????????? ?????? ????????? ?????? ")
    public void ??????_??????_??????_BAD_REQUEST() throws Exception {
        // given
        String accessToken = anotherLogin();
        Long reviewId = 1L;

        // when
        ExtractableResponse<Response> response = reportReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
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

    public static String anotherLogin() {
        LoginRequest request = LoginRequest.builder()
                .email("anotherEmail")
                .password(memberPassword)
                .build();

        return loginRequest(request)
                .as(LoginResponse.class)
                .getAccessToken();
    }

    private static ExtractableResponse<Response> reviewCreateRequest(String accessToken,ReviewCreateRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(APPLICATION_JSON_VALUE)
                .when().post("/reviews")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> reviewUpdateRequest(String accessToken, ReviewUpdateRequest request,Long reviewId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(APPLICATION_JSON_VALUE)
                .when().put("/reviews/{reviewId}",reviewId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> reviewDeleteRequest(String accessToken, Long reviewId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/reviews/{reviewId}", reviewId)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> reviewFindByReviewId(Long reviewId) {
        return RestAssured
                .given().log().all()
                .accept(APPLICATION_JSON_VALUE)
                .when().get("/reviews/{reviewId}", reviewId)
                .then().log().all()
                .extract();
    }


    private static ExtractableResponse<Response> searchReviewByMovieCode(String movieId) {
        return RestAssured
                .given().log().all()
                .accept(APPLICATION_JSON_VALUE)
                .when().get("/reviews/movies/{movieId}", movieId)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> searchMyReviewByMovieCode(String accessToken, String movieId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(APPLICATION_JSON_VALUE)
                .when().get("/reviews/me/{movieId}", movieId)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> recommendReview(String accessToken, Long reviewId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(APPLICATION_JSON_VALUE)
                .when().post("/reviews/recommend/{reviewId}", reviewId)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> reportReview(String accessToken, Long reviewId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(APPLICATION_JSON_VALUE)
                .when().post("/reviews/report/{reviewId}", reviewId)
                .then().log().all()
                .extract();
    }

}
