package com.moviePicker.api.review;

import com.moviePicker.api.AcceptanceTest;
import com.moviePicker.api.comment.repository.CommentRepository;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("reviewAcceptanceDataLoader")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class ReviewAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("테스트 01: 리뷰 작성 실패 401: 로그인하지 않았을떄")
    public void 리뷰_작성_실패_UNAUTHORIZED() throws Exception {
        // given


        // when

        // then

    }


    @Test
    @DisplayName("테스트 02: 리뷰 작성 실패 400: title/ content 가 잘못된 경우")
    public void 리뷰_작성_실패_BAD_REQUEST() throws Exception {
        // given


        // when

        // then

    }

    @Test
    @DisplayName("테스트 03: 리뷰 작성 실패 404: 잘못된 movie id 인 경우")
    public void 리뷰_작성_실패_NOT_FOUND() throws Exception {
        // given


        // when

        // then

    }

    @Test
    @DisplayName("테스트 04: 리뷰 작성 성공 200")
    public void 리뷰_작성_성공_OK() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 05: 리뷰 수정하기 실패 401: 로그인 안한 회원이 접근한 경우 ")
    public void 리뷰_수정_실패_UNAUTHORIZED() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 06: 리뷰 수정하기 실패 403: 사용자가 작성한 리뷰가 아닐때 ")
    public void 리뷰_수정_실패_FORIBIDDEN() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 07: 리뷰 수정하기 실패 404: review id 가 잘못된 경우")
    public void 리뷰_수정_실패_NOT_FOUND() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 08: 리뷰 수정하기 성공 200")
    public void 리뷰_수정_성공_OK() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 09: 리뷰 지우기 실패 401: 로그인 안한 회원이 접근한 경우 ")
    public void 리뷰_지우기_실패_UNAUTHORIZED() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 10:  리뷰 지우기 실패 403: 사용자가 작성한 리뷰가 아닐때 ")
    public void 리뷰_지우기_실패_FORIBIDDEN() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 11: 리뷰 지우기 실패 404: review id 가 잘못된 경우")
    public void 리뷰_지우기_실패_NOT_FOUND() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 12: 리뷰 지우기 성공 200")
    public void 리뷰_지우기_성공_OK() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 13: 리뷰아이디로 리뷰 조회 실패 404: 리뷰 ID 가 잘못된 경우")
    public void 리뷰아이디로_리뷰조회_실패_NOT_FOUND() throws Exception {
        // given

        // when

        // then

    }


    @Test
    @DisplayName("테스트 14: 리뷰아이디로 리뷰 조회 성공 200")
    public void 리뷰id로_리뷰조회_성공_OK() throws Exception {
        // given

        // when

        // then

    }


    @Test
    @DisplayName("테스트 15: 영화아이디로 리뷰 조회 실패 404: 잘못된 movie id인 경우")
    public void 영화로_리뷰_댓글조회_실패_NOT_FOUND() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 16: 영화아이디로 리뷰댓글 조회 성공 200")
    public void 영화로_리뷰_댓글조회_성공_OK() throws Exception {
        // given

        // when

        // then

    }


    @Test
    @DisplayName("테스트 17: 영화아이디로 내가 쓴 리뷰 불러오기 실패 401: 로그인 안한 호원이 접근한 경우")
    public void 영화로_내가쓴_리뷰_불러오기_실패_UNAUTHORIZED() throws Exception {
        // given

        // when

        // then

    }


    @Test
    @DisplayName("테스트 18: 영화아이디로 내가 쓴 리뷰 불러오기 성공 404: 잘못된 movie id인 경우")
    public void 영화로_내가쓴_리뷰_불러오기_실패_NOT_FOUND() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 19: 영화아이디로 내가 쓴 리뷰 불러오기 성공 200")
    public void 영화로_내가쓴_리뷰_불러오기_성공_OK() throws Exception {
        // given

        // when

        // then

    }


    @Test
    @DisplayName("테스트 20: 리뷰 추천 및 추천 취소 실패 401: 로그인 안한 회원이 접근한 경우 ")
    public void 리뷰_추천_취소_실패_UNAUTHORIZED() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 21: 리뷰 추천 및 추천 취소 실패 404: review id가 잘못된 경우 ")
    public void 리뷰_추천_취소_실패_NOT_FOUND() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 22: 리뷰 추천 및 추천 취소 성공 200 ")
    public void 리뷰_추천_취소_성공_OK() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 23: 리뷰 신고 실패 401: 로그인 안한 회원이 접근한 경우 ")
    public void 리뷰_신고_실패_UNAUTHORIZED() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 24: 리뷰 신고 실패 404: review id가 잘못된 경우 ")
    public void 리뷰_신고_실패_NOT_FOUND() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 25: 리뷰 신고 실패 400: 이미 해당 회원이 한번 신고한 경우 ")
    public void 리뷰_신고_실패_BAD_REQUEST() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 26: 리뷰 신고 성공 200 ")
    public void 리뷰_신고_성공_OK() throws Exception {
        // given

        // when

        // then

    }


}
