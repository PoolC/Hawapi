package com.moviePicker.api.comment.service;

import com.moviePicker.api.auth.exception.UnauthorizedException;
import com.moviePicker.api.comment.domain.Comment;
import com.moviePicker.api.comment.dto.CommentCreateRequest;
import com.moviePicker.api.comment.dto.CommentUpdateRequest;
import com.moviePicker.api.comment.repository.CommentRepository;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.member.service.MemberService;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.dto.ReviewResponse;
import com.moviePicker.api.review.dto.ReviewUpdateRequest;
import com.moviePicker.api.review.repository.ReviewRepository;
import com.moviePicker.api.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class CommentService {
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    public void createComment(Member member, CommentCreateRequest request) {
        Member targetMember = memberRepository.getById(member.getUUID());
        Review targetReview = reviewRepository.getById(request.getReviewId());
        Comment comment= Comment.of(targetMember, targetReview, request.getContent());
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Comment comment, CommentUpdateRequest request) {
        Comment updatingComment = commentRepository.getById(comment.getId());
        updatingComment.updateCommentInfo(request);
        commentRepository.saveAndFlush(updatingComment);
    }

    @Transactional
    public void deleteComment(Comment comment) {
        Comment deletingComment = commentRepository.getById(comment.getId());
        commentRepository.delete(deletingComment);
    }
}
