package com.moviePicker.api.review;

import com.moviePicker.api.AcceptanceTest;
import com.moviePicker.api.auth.dto.LoginRequest;
import com.moviePicker.api.auth.dto.LoginResponse;
import com.moviePicker.api.comment.repository.CommentRepository;
import com.moviePicker.api.member.dto.MemberCreateRequest;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.movie.MovieAcceptanceDataLoader;
import com.moviePicker.api.movie.dto.MoviesResponse;
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
import static com.moviePicker.api.movie.MovieAcceptanceDataLoader.*;
import static com.moviePicker.api.movie.MovieAcceptanceDataLoader.specificMovie;
import static com.moviePicker.api.review.ReviewAcceptanceDataLoader.memberNickname;
import static com.moviePicker.api.review.ReviewAcceptanceDataLoader.specificMovieCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("reviewAcceptanceDataLoader")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class ReviewAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("테스트 01: 리뷰 작성 실패 403: 로그인하지 않았을떄")
    public void 리뷰_작성_실패_FORBIDDEN() throws Exception {
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
    @DisplayName("테스트 02: 리뷰 작성 실패 400: title/content 가 잘못된 경우")
    public void 리뷰_작성_실패_BAD_REQUEST() throws Exception {
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
    @DisplayName("테스트 03: 리뷰 작성 실패 404: 잘못된 movie id 인 경우")
    public void 리뷰_작성_실패_NOT_FOUND() throws Exception {
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
    @DisplayName("테스트 04: 리뷰 작성 성공 200")
    public void 리뷰_작성_성공_OK() throws Exception {
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
        assertThat(reviewRepository.findByTitle(title)
        ).isNotEqualTo(null);
    }

    @Test
    @DisplayName("테스트 05: 리뷰 수정하기 실패 403: 로그인 안한 회원이 접근한 경우 ")
    public void 리뷰_수정_실패_FORBIDDEN() throws Exception {
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
    @DisplayName("테스트 06: 리뷰 수정하기 실패 401: 사용자가 작성한 리뷰가 아닐때 ")
    public void 리뷰_수정_실패_UNAUTHORIZED() throws Exception {
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
    @DisplayName("테스트 07: 리뷰 수정하기 실패 404: review id 가 잘못된 경우")
    public void 리뷰_수정_실패_NOT_FOUND() throws Exception {
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
    @DisplayName("테스트 08: 리뷰 수정하기 성공 200")
    public void 리뷰_수정_성공_OK() throws Exception {
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
    @DisplayName("테스트 09: 리뷰 지우기 실패 403: 로그인 안한 회원이 접근한 경우 ")
    public void 리뷰_지우기_실패_FORBIDDEN() throws Exception {
        // given
        String accessToken="";
        Long reviewId=1L;

        // when
        ExtractableResponse<Response> response = reviewDeleteRequest(accessToken,reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());
    }

    @Test
    @DisplayName("테스트 10:  리뷰 지우기 실패 401: 사용자가 작성한 리뷰가 아닐때 ")
    public void 리뷰_지우기_실패_UNAUTHORIZED() throws Exception {
        // given
        String accessToken=defaultLogin();
        Long reviewId=64L;

        // when
        ExtractableResponse<Response> response = reviewDeleteRequest(accessToken,reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("테스트 11: 리뷰 지우기 실패 404: review id 가 잘못된 경우")
    public void 리뷰_지우기_실패_NOT_FOUND() throws Exception {
        // given
        String accessToken=defaultLogin();
        Long reviewId=100L;

        // when
        ExtractableResponse<Response> response = reviewDeleteRequest(accessToken,reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @DisplayName("테스트 12: 리뷰 지우기 성공 200")
    public void 리뷰_지우기_성공_OK() throws Exception {
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
    @DisplayName("테스트 13: 리뷰아이디로 리뷰 조회 실패 404: 리뷰 ID 가 잘못된 경우")
    public void 리뷰아이디로_리뷰조회_실패_NOT_FOUND() throws Exception {
        // given
        Long reviewId=100L;

        // when
        ExtractableResponse<Response> response = reviewFindByReviewId(reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }


    @Test
    @DisplayName("테스트 14: 리뷰아이디로 리뷰 조회 성공 200")
    public void 리뷰아이디로_리뷰조회_성공_OK() throws Exception {
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
    @DisplayName("테스트 15: 영화아이디로 리뷰 조회 실패 404: 잘못된 movie id인 경우")
    public void 영화로_리뷰조회_실패_NOT_FOUND() throws Exception {

        // given
        String movieId = "wrongMovieCode";

        // when
        ExtractableResponse<Response> response = searchReviewByMovieCode(movieId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @DisplayName("테스트 16: 영화아이디로 리뷰댓글 조회 성공 200")
    public void 영화로_리뷰조회_성공_OK() throws Exception {

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
    @DisplayName("테스트 17: 영화아이디로 내가 쓴 리뷰 불러오기 실패 403: 로그인 안한 회원이 접근한 경우")
    public void 영화로_내가쓴_리뷰_불러오기_실패_FORBIDDEN() throws Exception {

        // given
        String accessToken = "";
        String movieId = specificMovieCode;

        // when
        ExtractableResponse<Response> response = searchMyReviewByMovieCode(accessToken, movieId);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());
    }


    @Test
    @DisplayName("테스트 18: 영화아이디로 내가 쓴 리뷰 불러오기 실패 404: movie id가 잘못 되었을때")
    public void 영화로_내가쓴_리뷰_불러오기_실패_NOT_FOUND() throws Exception {

        // given
        String accessToken = defaultLogin();
        String movieId = "wrongMovieCode";

        // when
        ExtractableResponse<Response> response = searchMyReviewByMovieCode(accessToken, movieId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @DisplayName("테스트 19: 영화아이디로 내가 쓴 리뷰 불러오기 성공 200")
    public void 영화로_내가쓴_리뷰_불러오기_성공_OK() throws Exception {

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
    @DisplayName("테스트 20: 리뷰 추천 및 추천 취소 실패 403: 로그인 안한 회원이 접근한 경우 ")
    public void 리뷰_추천취소_실패_FORBIDDEN() throws Exception {

        // given
        String accessToken = "";
        Long reviewId = 1L;

        // when
        ExtractableResponse<Response> response = recommendReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());
    }

    @Test
    @DisplayName("테스트 21: 리뷰 추천 및 추천 취소 실패 404: review id가 잘못된 경우 ")
    public void 리뷰_추천취소_실패_NOT_FOUND() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long reviewId = -33L;

        // when
        ExtractableResponse<Response> response = recommendReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    @DisplayName("테스트 22: 리뷰 추천 성공 200 ")
    public void 리뷰_추천_성공_OK() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long reviewId = 1L;

        // when
        ExtractableResponse<Response> response = recommendReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(reviewRepository.findById(reviewId).get().getRecommendationCount().equals(1));
    }

    @Test
    @DisplayName("테스트 23: 리뷰 추천 취소 성공 200 ")
    public void 리뷰_추천_취소_성공_OK() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long reviewId = 1L;

        // when
        ExtractableResponse<Response> response = recommendReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(reviewRepository.findById(reviewId).get().getRecommendationCount().equals(0));
    }

    @Test
    @DisplayName("테스트 24: 리뷰 신고 실패 403: 로그인 안한 회원이 접근한 경우 ")
    public void 리뷰_신고_실패_FORBIDDEN() throws Exception {

        // given
        String accessToken = "";
        Long reviewId = 1L;

        // when
        ExtractableResponse<Response> response = reportReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(FORBIDDEN.value());

    }

    @Test
    @DisplayName("테스트 25: 리뷰 신고 실패 404: review id가 잘못된 경우 ")
    public void 리뷰_신고_실패_NOT_FOUND() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long reviewId = -33L;

        // when
        ExtractableResponse<Response> response = reportReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }

    @Test
    @DisplayName("테스트 26: 리뷰 신고 성공 200 ")
    public void 리뷰_신고_성공_OK() throws Exception {
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
    @DisplayName("테스트 27: 리뷰 신고 실패 400: 이미 해당 회원이 한번 신고한 경우 ")
    public void 리뷰_신고_실패_BAD_REQUEST() throws Exception {
        // given
        String accessToken = defaultLogin();
        Long reviewId = 1L;

        // when
        ExtractableResponse<Response> response = reportReview(accessToken, reviewId);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }


    public static String defaultLogin() {
        LoginRequest request = LoginRequest.builder()
                .email(memberEmail)
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
