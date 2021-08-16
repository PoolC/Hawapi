package com.moviePicker.api.movie;


import com.moviePicker.api.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static com.moviePicker.api.movie.MovieAcceptanceDataLoader.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("movieAcceptanceDataLoader")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class MovieAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테스트 01: 현재 상영중인 영화들 실패 404 : 잘못된 페이지 쿼리 파라미터 입력했을때 ")
    public void 현재_상영중인_영화들_실패_NOT_FOUND() throws Exception {

        //given
        int invalidPageNumber=4;

        //when
        ExtractableResponse<Response> response=searchRunningMovie(invalidPageNumber);

        //then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }

    @Test
    @DisplayName("테스트 02: 현재 상영중인 영화들 성공 200")
    public void 현재_상영중인_영화들_성공_200() throws Exception {

        //given
        int validPageNumber=1;

        //when
        ExtractableResponse<Response> response=searchRunningMovie(validPageNumber);

        //then
        assertThat(response.body().jsonPath().getList("movies.movieId")).isEqualTo(boxOfficeMovieCode);
        assertThat(response.statusCode()).isEqualTo(OK.value());

    }

    @Test
    @DisplayName("테스트 03: 검색어로 영화조회 실패 400 : 검색어를 입력하지 않았을때")
    public void 검색어로_영화조회_실패_BAD_REQUEST() throws Exception {
        //given
        String invalidQuery="";
        int validPageNumber=1;

        //when
        ExtractableResponse<Response> response=searchMovieBySearchingWord(invalidQuery,validPageNumber);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    @DisplayName("테스트 04: 검색어로 영화조회 실패 404 : 잘못된 페이지 뭐리 파라미터 입력했을때")
    public void 검색어로_영화조회_실패_NOT_FOUND() throws Exception {

        //given
        String validQuery="validQuery";
        int invalidPageNumber=4;

        //when
        ExtractableResponse<Response> response=searchMovieBySearchingWord(validQuery,invalidPageNumber);

        //then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());

    }

    @Test
    @DisplayName("테스트 05: 검색어로 영화조회 성공 200")
    public void 검색어로_영화조회_성공_OK() throws Exception {

        //given
        String validQuery="validQuery";
        int validPageNumber=1;

        //when
        ExtractableResponse<Response> response=searchMovieBySearchingWord(validQuery,validPageNumber);

        //then
        //추가적으로 내용이 맞는지 확인하는 테스트코드 필요
        assertThat(response.statusCode()).isEqualTo(OK.value());

    }

    @Test
    @DisplayName("테스트 06: 영화 아이디로 영화조회 실패 400 : movieId가 잘못된 경우")
    public void 영화_아이디로_영화조회_실패_BAD_REQUEST() throws Exception {

        //given
        String notExistingMovieId="notExistingMovieId";

        //when
        ExtractableResponse<Response> response=searchMovieByMovieId(notExistingMovieId);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }

    @Test
    @DisplayName("테스트 07: 영화 아이디로 영화조회 성공 200")
    public void 영화_아이디로_영화조회_성공_OK() throws Exception {

        //given
        String existingMovieId="specificMovieCode";

        //when
        ExtractableResponse<Response> response=searchMovieByMovieId(existingMovieId);

        //then
        assertThat(response.body().jsonPath().getString("movie.movieId")).isEqualTo(specificMovieCode);
        assertThat(response.statusCode()).isEqualTo(OK.value());

    }

    @Test
    @DisplayName("테스트 08: 리뷰 아이디로 영화조회 실패 400 :  reviewId가 잘못된 경우")
    public void 리뷰_아이디로_영화조회_실패_BAD_REQUEST() throws Exception {

        //given
        Long notExistingReviewId=2L;

        //when
        ExtractableResponse<Response> response=searchMovieByReviewId(notExistingReviewId);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }

    @Test
    @DisplayName("테스트 09: 리뷰 아이디로 영화조회 성공 200")
    public void 리뷰_아이디로_영화조회_성공_OK() throws Exception {

        //given

        //when
        ExtractableResponse<Response> response=searchMovieByReviewId(specificReviewId);

        //then
        assertThat(response.body().jsonPath().getString("movie.movieId")).isEqualTo(specificMovieCode);
        assertThat(response.statusCode()).isEqualTo(OK.value());

    }





    private static ExtractableResponse<Response> searchRunningMovie(int pageNumber) {
        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/nowadays?page=:pageNumber",pageNumber)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> searchMovieBySearchingWord(String query, int pageNumber) {
        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/search/:query?page=:pageNumber",query,pageNumber)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> searchMovieByMovieId(String movieId) {
        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/:movieId",movieId)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> searchMovieByReviewId(Long reviewId) {
        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/:reviewsId",reviewId)
                .then().log().all()
                .extract();
    }


}
