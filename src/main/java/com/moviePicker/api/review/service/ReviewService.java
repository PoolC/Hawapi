package com.moviePicker.api.review.service;


import com.moviePicker.api.auth.exception.UnauthenticatedException;
import com.moviePicker.api.comment.domain.Comment;
import com.moviePicker.api.comment.repository.CommentRepository;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.member.service.MemberService;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.domain.MovieWished;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.movie.service.MovieService;
import com.moviePicker.api.review.domain.Recommendation;
import com.moviePicker.api.review.domain.Report;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.dto.ReviewCreateRequest;
import com.moviePicker.api.review.dto.ReviewResponse;
import com.moviePicker.api.review.dto.ReviewUpdateRequest;
import com.moviePicker.api.review.repository.RecommendationRepository;
import com.moviePicker.api.review.repository.ReportRepository;
import com.moviePicker.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {


    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RecommendationRepository recommendationRepository;
    private final ReportRepository reportRepository;
    private final MovieService movieService;
    private final MemberService memberService;
    private final CommentRepository commentRepository;

    public void createReview(Member member,ReviewCreateRequest reviewCreateRequest){
        Movie targetMovie= movieService.searchMovieByMovieCode(reviewCreateRequest.getMovieId());
        Member targetMember=memberService.getMemberByEmail(member.getEmail());
        Review review=Review.of(targetMember,targetMovie,reviewCreateRequest.getTitle(),reviewCreateRequest.getContent());
        reviewRepository.save(review);
    }

    @Transactional
    public void updateReview(Member member, ReviewUpdateRequest reviewCreateRequest,Long reviewId){
        Review targetReview = getReviewByReviewId(reviewId);
        Member targetMember=memberService.getMemberByEmail(member.getEmail());
        checkReviewMember(targetReview, targetMember);
        targetReview.setTitle(reviewCreateRequest.getTitle());
        targetReview.setContent(reviewCreateRequest.getContent());
        reviewRepository.saveAndFlush(targetReview);
    }

    @Transactional
    public void deleteReview(Member member,Long reviewId){
        Review targetReview = getReviewByReviewId(reviewId);
        Member targetMember=memberService.getMemberByEmail(member.getEmail());
        checkReviewMember(targetReview, targetMember);
        removeReviewComment(targetReview);
        removeRecommendation(targetReview);
        removeReport(targetReview);
        reviewRepository.deleteById(reviewId);
    }

    public Review searchByReviewId(Long reviewId){
        return getReviewByReviewId(reviewId);
    }

    public List<ReviewResponse> searchReviewByMovieId(Movie movie) {
        return reviewRepository.findByMovie(movie).stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> searchMyReviewByMovieId(Member member, Movie movie) {
        return reviewRepository.findByMovieAndMember(movie, member).stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }


    public void recommendReview(Member member, Review review) {
        if (isNewRecommendation(member, review)) {
            newRecommendation(member, review);
            return;
        }
        cancelRecommendation(member, review);
    }


    public void reportReview(Member member, Review review) {
        if (isNewReport(member, review)) {
            newReport(member, review);
            return;
        }
        throw new IllegalArgumentException("이미 신고하셨습니다");
    }

    @Transactional
    public void newRecommendation(Member member, Review review) {
        Member recommendingMember = memberRepository.getById(member.getUUID());
        Review recommendedReview = reviewRepository.getById(review.getId());

        recommendationRepository.save(Recommendation.of(recommendingMember, recommendedReview));
        recommendedReview.addRecommendationCount();
        reviewRepository.saveAndFlush(recommendedReview);
    }

    @Transactional
    public void cancelRecommendation(Member member, Review review) {
        Member unRecommendingMember = memberRepository.getById(member.getUUID());
        Review unRecommendedReview = reviewRepository.getById(review.getId());
        Optional<Recommendation> recommendation = recommendationRepository.findByMemberAndReview(unRecommendingMember, unRecommendedReview);

        recommendationRepository.delete(recommendation.get());
        unRecommendedReview.subtractRecommendationCount();
        reviewRepository.save(unRecommendedReview);
    }

    @Transactional
    public void newReport(Member member, Review review) {
        Member reportingMember = memberRepository.getById(member.getUUID());
        Review reportedReview = reviewRepository.getById(review.getId());

        reportRepository.save(Report.of(reportingMember, reportedReview));
        reportedReview.addReportCount();
        reviewRepository.saveAndFlush(reportedReview);
    }

    private boolean isNewRecommendation(Member member, Review review) {
        return recommendationRepository.findByMemberAndReview(member, review).isEmpty();
    }

    private boolean isNewReport(Member member, Review review) {
        return reportRepository.findByMemberAndReview(member, review).isEmpty();
    }

    private Review getReviewByReviewId(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new NoSuchElementException("존재하지 않는 reviewId입니다"));
    }

    private void checkReviewMember(Review review, Member member) {
        if(!member.getReviewList().contains(review)){
            throw new UnauthenticatedException("해당 사용자가 작성한 리뷰가 아닙니다");
        }
    }

    private void removeReviewComment(Review review) {
        for(Comment comment: review.getCommentList()){
            commentRepository.delete(comment);
        }
    }

    private void removeReport(Review review) {
        for(Report report: review.getReportList()){
            reportRepository.delete(report);
        }
    }

    private void removeRecommendation(Review review) {
        for(Recommendation recommendation: review.getRecommendationList()){
            recommendationRepository.delete(recommendation);
        }
    }

}
