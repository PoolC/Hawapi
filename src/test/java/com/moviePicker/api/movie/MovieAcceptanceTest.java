package com.moviePicker.api.movie;


import com.moviePicker.api.AcceptanceTest;
import com.moviePicker.api.member.dto.MemberCreateRequest;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.repository.MovieRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("movieAcceptanceDataLoader")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class MovieAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테스트 01: 현재 상영중인 영화들 성공 200 ")
    public void 현재_상영중인_영화들_성공_OK() throws Exception {

        //given
        int pageNum=1;

        //when
        ExtractableResponse<Response> response=searchRunningMovie(pageNum);

        //then


    }

    @Test
    @DisplayName("테스트 02: 검색어로 영화조회 실패 400 : 키워드를 입력하지 않았을때")
    public void 검색어로_영화조회_실패_BAD_REQUEST_1() throws Exception {

    }

    @Test
    @DisplayName("테스트 03: 검색어로 영화조회 성공 200")
    public void 검색어로_영화조회_성공_OK() throws Exception {

    }

    @Test
    @DisplayName("테스트 04: 영화 아이디로 영화조회 실패 400 : movieId가 잘못된 경우")
    public void 영화_아이디로_영화조회_실패_BAD_REQUEST() throws Exception {

        //given
        String notExistingMovieId="notExistingMovieId";

        //when
        ExtractableResponse<Response> response=searchMovieByMovieId(notExistingMovieId);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }

    @Test
    @DisplayName("테스트 05: 영화 아이디로 영화조회 성공 200")
    public void 영화_아이디로_영화조회_성공_OK() throws Exception {

        //given
        String existingMovieId="existingMovieId";

        //when
        ExtractableResponse<Response> response=searchMovieByMovieId(existingMovieId);

        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());


    }

    @Test
    @DisplayName("테스트 06: 리뷰 아이디로 영화조회 실패 400 :  reviewId가 잘못된 경우")
    public void 리뷰_아이디로_영화조회_실패_BAD_REQUEST() throws Exception {

        //given
        String notExistingReviewId="notExistingReviewId";

        //when
        ExtractableResponse<Response> response=searchMovieByMovieId(notExistingReviewId);

        //then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());

    }

    @Test
    @DisplayName("테스트 07: 리뷰 아이디로 영화조회 성공 200")
    public void 리뷰_아이디로_영화조회_성공_OK() throws Exception {

        //given
        String ExistingReviewId="notExistingReviewId";

        //when
        ExtractableResponse<Response> response=searchMovieByMovieId(ExistingReviewId);

        //then
        assertThat(response.statusCode()).isEqualTo(OK.value());

    }





    private static ExtractableResponse<Response> searchRunningMovie(int pageNumber) {
        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/nowadays/{pageNumber}}",pageNumber)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> searchMovieByMovieId(String movieId) {
        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/nowadays/{movieId}}",movieId)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> searchMovieByReviewId(String reviewId) {
        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/movies/nowadays/{reviewId}}",reviewId)
                .then().log().all()
                .extract();
    }


}
