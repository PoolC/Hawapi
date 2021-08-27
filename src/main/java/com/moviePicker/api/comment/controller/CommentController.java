package com.moviePicker.api.comment.controller;

import com.moviePicker.api.auth.exception.UnauthenticatedException;
import com.moviePicker.api.auth.exception.UnauthorizedException;
import com.moviePicker.api.comment.domain.Comment;
import com.moviePicker.api.comment.dto.CommentCreateRequest;
import com.moviePicker.api.comment.dto.CommentUpdateRequest;
import com.moviePicker.api.comment.repository.CommentRepository;
import com.moviePicker.api.comment.service.CommentService;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.dto.ReviewCreateRequest;
import com.moviePicker.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    @PostMapping
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal Member member, @RequestBody @Valid CommentCreateRequest request){
        validateRequest(request);
        commentService.createComment(member,request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal Member member, @RequestBody @Valid CommentUpdateRequest request, @PathVariable("commentId") Long commentId){
        Comment comment = validateRequest(member, commentId);
        commentService.updateComment(comment, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal Member member,@PathVariable("commentId") Long commentId){
        Comment comment = validateRequest(member, commentId);
        commentService.deleteComment(comment);
        return ResponseEntity.ok().build();
    }

    private void validateRequest(CommentCreateRequest request){
        findReview(request.getReviewId());
    }

    private Review findReview(Long reviewId){
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.orElseThrow(() -> new NoSuchElementException("존재하지 않는 리뷰입니다."));
    }


    private Comment validateRequest(Member member, Long commentId){
        Comment comment = findComment(commentId);
        checkMemberMatch(member, comment);
        return comment;
    }

    private Comment findComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.orElseThrow(() -> new NoSuchElementException("존재하지 않는 댓글입니다."));
    }

    private void checkMemberMatch(Member member, Comment comment) {
        if (!member.getEmail().equals(comment.getMember().getEmail())){
            throw new UnauthenticatedException("자신이 쓴 댓글만 수정/삭제 가능합니다.");
        }
    }

}
