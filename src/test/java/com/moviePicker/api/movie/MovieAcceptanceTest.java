package com.moviePicker.api.movie;


import com.moviePicker.api.AcceptanceTest;
import com.moviePicker.api.auth.dto.LoginRequest;
import com.moviePicker.api.auth.dto.LoginResponse;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.movie.dto.MovieResponse;
import com.moviePicker.api.movie.dto.MoviesResponse;
import com.moviePicker.api.movie.dto.WishRegisterResponse;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.movieWatched.repository.MovieWatchedRepository;
import com.moviePicker.api.movieWished.repository.MovieWishedRepository;
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
import static com.moviePicker.api.movie.MovieAcceptanceDataLoader.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("movieAcceptanceDataLoader")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class MovieAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MovieWishedRepository movieWishedRepository;

    @Autowired
    private MovieWatchedRepository movieWatchedRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("테스트 01: 현재 상영중인 영화목록조회 실패 404 : 잘못된 페이지 쿼리 파라미터 입력했을때 ")
    public void 현재_상영중인_영화목록_실패_NOT_FOUND() throws Exception {

        //given
        int invalidPageNumber = 4;

        //when
        ExtractableResponse<Response> response = searchMoviesRunning(invalidPageNumber);

        //then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }

    @Test
    @DisplayName("테스트 02: 현재 상영중인 영화목록조회 성공 200")
    public void 현재_상영중인_영화목록조회_성공() throws Exception {

        //given
        int validPageNumber = 1;
        int expectingSize = sizeOfPage;
        //when
        ExtractableResponse<Response> response = searchMoviesRunning(validPageNumber);
        MoviesResponse responseBody = response.as(MoviesResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responseBody.getMovies()).hasSize(expectingSize);

    }

    @Test
    @DisplayName("테스트 03: 검색어로 영화목록조회 실패 400 : 검색어를 입력하지 않았을때")
    public void 검색어로_영화목록조회_실패_BAD_REQUEST() throws Exception {
        //given
        String invalidQuery = "";
        int validPageNumber = 1;

        //when
        ExtractableResponse<Response> response = searchMoviesByQuery(invalidQuery, validPageNumber);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    @DisplayName("테스트 04: 검색어로 영화목록조회 실패 404 : 잘못된 페이지 쿼리 파라미터 입력했을때")
    public void 검색어로_영화목록조회_실패_NOT_FOUND() throws Exception {

        //given
        String validQuery = "validQuery";
        int invalidPageNumber = 4;

        //when
        ExtractableResponse<Response> response = searchMoviesByQuery(validQuery, invalidPageNumber);

        //then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }

    @Test
    @DisplayName("테스트 05: 검색어로 영화목록조회 성공 200")
    public void 검색어로_영화목록조회_성공_OK() throws Exception {

        //given
        String validQuery = "validQuery";
        int validPageNumber = 1;

        //when
        ExtractableResponse<Response> response = searchMoviesByQuery(validQuery, validPageNumber);
        MoviesResponse responseBody = response.as(MoviesResponse.class);

        //then
        //추가적으로 내용이 맞는지 확인하는 테스트코드 필요
        assertThat(response.statusCode()).isEqualTo(OK.value());

    }


    @Test
    @DisplayName("테스트 06: 보고싶은 영화목록조회 실패 401 : 로그인 안한 회원이 접근한 경우")
    public void 보고싶은_영화목록조회_실패_UNAUTHORIZED() throws Exception {

        // given
        String accessToken = "";
        int pageNumber = 1;

        // when
        ExtractableResponse<Response> response = searchMoviesWished(accessToken, pageNumber);


        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("테스트 07: 보고싶은 영화목록조회 실패 404 : 잘못된 페이지 쿼리 파라미터 입력했을떄")
    public void 보고싶은_영화목록조회_실패_NOT_FOUND() throws Exception {
        // given
        String accessToken = defaultLogin();
        int pageNumber = 4;

        // when
        ExtractableResponse<Response> response = searchMoviesWished(accessToken, pageNumber);


        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }

    @Test
    @DisplayName("테스트 08: 보고싶은 영화목록조회 성공 200")
    public void 보고싶은_영화목록조회_성공_OK() throws Exception {
        // given
        String accessToken = defaultLogin();
        int pageNumber = 1;
        int expectingSize = sizeOfPage;

        // when
        ExtractableResponse<Response> response = searchMoviesWished(accessToken, pageNumber);
        MoviesResponse responseBody = response.as(MoviesResponse.class);


        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responseBody.getMovies()).hasSize(expectingSize);

    }


    @Test
    @DisplayName("테스트 09: 이미본 영화목록조회 실패 401 : 로그인 안한 회원이 접근한 경우")
    public void 이미본_영화목록조회_실패_UNAUTHORIZED() throws Exception {
        // given
        String accessToken = "";
        int pageNumber = 1;

        // when
        ExtractableResponse<Response> response = searchMoviesWatched(accessToken, pageNumber);


        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());

    }

    @Test
    @DisplayName("테스트 10: 이미본 영화목록조회 실패 404 : 잘못된 페이지 쿼리 파라미터 입력했을떄 ")
    public void 이미본_영화목록조회_실패_NOT_FOUND() throws Exception {
        // given
        String accessToken = defaultLogin();
        int pageNumber = 4;

        // when
        ExtractableResponse<Response> response = searchMoviesWished(accessToken, pageNumber);


        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }

    @Test
    @DisplayName("테스트 11: 이미본 영화목록조회 성공 200")
    public void 이미본_영화목록조회_성공_OK() throws Exception {
        // given
        String accessToken = defaultLogin();
        int pageNumber = 1;
        int expectingSize = sizeOfPage;

        // when
        ExtractableResponse<Response> response = searchMoviesWished(accessToken, pageNumber);
        MoviesResponse responseBody = response.as(MoviesResponse.class);


        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responseBody.getMovies()).hasSize(expectingSize);

    }


    @Test
    @DisplayName("테스트 12:영화 아이디로 영화조회 실패 400 : movieId가 잘못된 경우")
    public void 영화_아이디로_영화조회_실패_BAD_REQUEST() throws Exception {

        //given
        String invalidMovieCode = "invalidMovieCode";

        //when
        ExtractableResponse<Response> response = searchMovieByMovieCode(invalidMovieCode);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }

    @Test
    @DisplayName("테스트 13: 영화 아이디로 영화조회 성공 200")
    public void 영화_아이디로_영화조회_성공_OK() throws Exception {

        //given
        String valideMovieCode = specificMovieCode;

        //when
        ExtractableResponse<Response> response = searchMovieByMovieCode(valideMovieCode);
        MovieResponse responseBody = response.as(MovieResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responseBody.getMovieCode().equals(specificMovieCode));


    }

    @Test
    @DisplayName("테스트 14: 리뷰 아이디로 영화조회 실패 400 : reviewId가 잘못된 경우")
    public void 리뷰_아이디로_영화조회_실패_BAD_REQUEST() throws Exception {

        //given
        Long invalidReviewId = 2L;

        //when
        ExtractableResponse<Response> response = searchMovieByReviewId(invalidReviewId);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }

    @Test
    @DisplayName("테스트 15: 리뷰 아이디로 영화조회 성공 200")
    public void 리뷰_아이디로_영화조회_성공_OK() throws Exception {

        //given
        Long validReviewId = specificReviewId;

        //when
        ExtractableResponse<Response> response = searchMovieByReviewId(validReviewId);
        MovieResponse responseBody = response.as(MovieResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responseBody.getMovieCode()).isEqualTo(specificMovieCode);

    }


    @Test
    @DisplayName("테스트 16: 보고싶은 영화등록 실패 401 : 로그인 안한 회원이 접근한 경우 ")
    public void 보고싶은_영화등록_실패_UNAUTHORIZED() throws Exception {

        //given
        String accessToken = "";

        //when
        ExtractableResponse<Response> response = registerWishMovie(accessToken, specificMovieCode);

        //then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("테스트 17: 보고싶은 영화등록 실패 400 : movieId가 잘못된 경우 ")
    public void 보고싶은_영화등록_실패_BAD_REQUEST() throws Exception {

        //given
        String accessToken = defaultLogin();
        String invalidMovieCode = "invalidMovieCode";

        //when
        ExtractableResponse<Response> response = registerWishMovie(accessToken, invalidMovieCode);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }


    @Test
    @DisplayName("테스트 18: 보고싶은 영화등록 성공 200 ")
    public void 보고싶은_영화등록_성공_OK() throws Exception {

        //given
        String accessToken = defaultLogin();
        String validMovieCode = specificMovieCode;


        //when
        ExtractableResponse<Response> response = registerWishMovie(accessToken, validMovieCode);
        WishRegisterResponse responseBody = response.as(WishRegisterResponse.class);


        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertTrue(responseBody.getIsRegistered());
        assertThat(movieWishedRepository.findByMemberAndMovie(defaultMember, specificMovie)
                .orElseThrow()
                .getMovie().getMovieCode()
        ).isEqualTo(specificMovieCode);


    }

    @Test
    @DisplayName("테스트 19: 보고싶은 영화해제 성공 200 ")
    public void 보고싶은_영화해제_성공_OK() throws Exception {

        //given
        String accessToken = defaultLogin();
        String validMovieCode = specificMovieCode;


        //when
        ExtractableResponse<Response> response = registerWishMovie(accessToken, validMovieCode);
        WishRegisterResponse responseBody = response.as(WishRegisterResponse.class);


        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertFalse(responseBody.getIsRegistered());
        assertTrue(movieWishedRepository.findByMemberAndMovie(defaultMember, specificMovie).isEmpty());


    }

    @Test
    @DisplayName("테스트 20: 이미 본 영화등록 실패 401 : 로그인 안한 회원이 접근한 경우")
    public void 이미본_영화등록_실패_UNAUTHORIZED() throws Exception {

        //given
        String accessToken = "";

        //when
        ExtractableResponse<Response> response = registerWatchedMovie(accessToken, specificMovieCode);

        //then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("테스트 21: 이미 본 영화등록 실패 400 : movieId가 잘못된 경우")
    public void 이미본_영화등록_실패_BAD_REQUEST() throws Exception {

        //given
        String accessToken = defaultLogin();
        String invalidMovieCode = "invalidMovieCode";

        //when
        ExtractableResponse<Response> response = registerWatchedMovie(accessToken, invalidMovieCode);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }


    @Test
    @DisplayName("테스트 22: 이미 본 영화등록 성공 200")
    public void 이미본_영화등록_성공_OK() throws Exception {

        //given
        String accessToken = defaultLogin();
        String validMovieCode = specificMovieCode;


        //when
        ExtractableResponse<Response> response = registerWatchedMovie(accessToken, validMovieCode);
        WishRegisterResponse responseBody = response.as(WishRegisterResponse.class);


        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertTrue(responseBody.getIsRegistered());
        assertThat(movieWatchedRepository.findByMemberAndMovie(defaultMember, specificMovie)
                .orElseThrow()
                .getMovie().getMovieCode()
        ).isEqualTo(specificMovieCode);


    }

    @Test
    @DisplayName("테스트 23: 이미 본 영화해제 성공 200")
    public void 이미본_영화해제_성공_OK() throws Exception {

        //given
        String accessToken = defaultLogin();
        String validMovieCode = specificMovieCode;
        
        //when
        ExtractableResponse<Response> response = registerWatchedMovie(accessToken, validMovieCode);
        WishRegisterResponse responseBody = response.as(WishRegisterResponse.class);


        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertFalse(responseBody.getIsRegistered());
        assertTrue(movieWatchedRepository.findByMemberAndMovie(defaultMember, specificMovie).isEmpty());


    }


    private static ExtractableResponse<Response> searchMoviesRunning(int pageNumber) {
        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/nowadays/{pageNumber}", pageNumber)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> searchMoviesByQuery(String query, int pageNumber) {
        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/search/{query}?page={pageNumber}", query, pageNumber)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> searchMoviesWished(String accessToken, int pageNumber) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/wish?page={pageNumber}", pageNumber)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> searchMoviesWatched(String accessToken, int pageNumber) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/watched?page={pageNumber}", pageNumber)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> searchMovieByMovieCode(String movieCode) {
        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/nowadays/{movieId}", movieCode)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> searchMovieByReviewId(Long reviewId) {
        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/nowadays/{reviewId}", reviewId)
                .then().log().all()
                .extract();
    }


    private static ExtractableResponse<Response> registerWishMovie(String accessToken, String movieId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/wish/{movieId}", movieId)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> registerWatchedMovie(String accessToken, String movieId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/watched/{movieId}", movieId)
                .then().log().all()
                .extract();
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


}
