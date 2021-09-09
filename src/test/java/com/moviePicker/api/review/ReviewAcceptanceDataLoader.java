package com.moviePicker.api.review;

import com.moviePicker.api.auth.infra.PasswordHashProvider;
import com.moviePicker.api.comment.domain.Comment;
import com.moviePicker.api.comment.repository.CommentRepository;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.domain.MemberRole;
import com.moviePicker.api.member.domain.MemberRoles;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.review.domain.Recommendation;
import com.moviePicker.api.review.domain.Report;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.repository.RecommendationRepository;
import com.moviePicker.api.review.repository.ReportRepository;
import com.moviePicker.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Profile("reviewAcceptanceDataLoader")
@RequiredArgsConstructor
public class ReviewAcceptanceDataLoader implements ApplicationRunner {
    private final MovieRepository movieRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final PasswordHashProvider passwordHashProvider;
    private final ReportRepository reportRepository;
    private final RecommendationRepository recommendationRepository;


    public final static String memberEmail = "defaultEmail@gmail.com",
            memberNickname = "존재하는닉네임",
            memberPassword = "password123!",
            specificMovieCode = "movie0";
    public static Long commentIdOfAnotherMember;
    public final static List<Review> reviewList = new ArrayList<>();
    public final static List<Movie> movieList = new ArrayList<>();
    public final static List<Comment> commentList = new ArrayList<>();
    public static Member defaultMember;
    public static Member anotherMember;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        generateMembers();
        generateReviewsReportsRecommendation();
        generateComments();
    }
    
    private void generateMembers() {
        defaultMember = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .email(memberEmail)
                .nickname(memberNickname)
                .passwordHash(passwordHashProvider.encodePassword(memberPassword))
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .authorizationToken(null)
                .authorizationTokenValidUntil(null)
                .reportCount(0)
                .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                .build();
        anotherMember = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .email("anotherEmail")
                .nickname("anotherMemberNickname")
                .passwordHash(passwordHashProvider.encodePassword(memberPassword))
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .authorizationToken(null)
                .authorizationTokenValidUntil(null)
                .reportCount(0)
                .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                .build();

        memberRepository.save(defaultMember);
        memberRepository.save(anotherMember);
    }


    private void generateReviewsReportsRecommendation() {
        for (int i = 0; i < 21; i++) {
            Movie movie = Movie.builder()
                    .movieCode("movie" + i)
                    .build();
            Review review = Review.of(defaultMember, movie, "reviewTitle" + i, "reviewContent" + i);
            Report report=Report.of(anotherMember,review);
            Recommendation recommendation=Recommendation.of(defaultMember,review);
            movieRepository.save(movie);
            reviewRepository.save(review);
            reportRepository.save(report);
            recommendationRepository.save(recommendation);
            movieList.add(movie);
            reviewList.add(review);
        }
        Movie anotherMovie = Movie.builder()
                .movieCode("anotherMovie")
                .build();
        Review anotherReview = Review.of(anotherMember,anotherMovie,"anotherTitle","anotherContent");
        movieRepository.save(anotherMovie);
        reviewRepository.save(anotherReview);
    }

    private void generateComments() {
        for (int i = 0; i < 21; i++) {
            Comment comment = Comment.of(defaultMember,  reviewList.get(i), "comment" + i);
            commentRepository.save(comment);
            commentList.add(comment);
        }
        Comment commentOfAnotherMember = Comment.of(anotherMember, reviewList.get(0),"comment21" );
        commentRepository.save(commentOfAnotherMember);
        commentList.add(commentOfAnotherMember);
        commentIdOfAnotherMember = commentRepository.findCommentByContent("comment21").get().getId();
    }
}
