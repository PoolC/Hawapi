package com.moviePicker.api.review.service;


import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.review.domain.Recommendation;
import com.moviePicker.api.review.domain.Report;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.dto.ReviewResponse;
import com.moviePicker.api.review.repository.RecommendationRepository;
import com.moviePicker.api.review.repository.ReportRepository;
import com.moviePicker.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {


    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RecommendationRepository recommendationRepository;
    private final ReportRepository reportRepository;

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


}
