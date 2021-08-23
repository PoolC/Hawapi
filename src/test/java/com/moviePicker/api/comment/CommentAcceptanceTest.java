package com.moviePicker.api.comment;

import com.moviePicker.api.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommentAcceptanceTest extends AcceptanceTest {


    @Test
    @DisplayName("테스트 09: 댓글 작성 실패 401 : 로그인 안한 회원이 접근한 경우")
    public void 댓글_작성_실패_UNAUTHORIZED() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 10: 댓글 작성 실패 404 : review id 가 잘못되 경우")
    public void 댓글_작성_실패_NOT_FOUND() throws Exception {
        // given

        // when

        // then

    }


    @Test
    @DisplayName("테스트 11: 댓글 작성 실패 400 : comment/content에 해당하는 글이 너무 많거나 없을 때 ")
    public void 댓글_작성_실패_BAD_REQUEST() throws Exception {
        // given

        // when

        // then

    }


    @Test
    @DisplayName("테스트 12: 댓글 작성 성공 200 ")
    public void 댓글_작성_성공_OK() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 16: 자신이 쓴 댓글 지우기 실패 401: 로그인 안한 회원이 접근한 경우 ")
    public void 댓글_지우기_실패_UNAUTHORIZED() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 17: 자신이 쓴 댓글 지우기 실패 403: 사용자가 작성한 댓글이 아닐때 ")
    public void 댓글_지우기_실패_FORIBIDDEN() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 18: 자신이 쓴 댓글 지우기 실패 404: comment id 가 잘못된 경우")
    public void 댓글_지우기_실패_NOT_FOUND() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 19: 자신이 쓴 댓글 지우기 성공 200")
    public void 댓글_지우기_성공_OK() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 28: 댓글 수정하기 실패 401: 로그인 안한 회원이 접근한 경우 ")
    public void 댓글_수정_실패_UNAUTHORIZED() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 29: 댓글 수정하기 실패 403: 사용자가 작성한 댓글이 아닐때 ")
    public void 댓글_수정_실패_FORIBIDDEN() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 30: 댓글 수정하기 실패 404: comment id 가 잘못된 경우")
    public void 댓글_수정_실패_NOT_FOUND() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("테스트 31: 댓글 수정하기 성공 200")
    public void 댓글_수정_성공_OK() throws Exception {
        // given

        // when

        // then

    }


}
