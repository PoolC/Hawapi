package com.moviePicker.api.review.domain;


import com.moviePicker.api.comment.domain.Comment;
import com.moviePicker.api.common.domain.TimestampEntity;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.reviewRecommended.domain.ReviewRecommended;
import com.moviePicker.api.reviewReported.domain.ReviewReported;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "reviews")
@Getter
public class Review extends TimestampEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "report_count")
    private int report_count;

    @Column(name = "recommendation_count")
    private int recommendation_count;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @OneToMany(mappedBy = "review")
    List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "review")
    List<ReviewRecommended> reviewRecommendedList = new ArrayList<>();

    @OneToMany(mappedBy = "review")
    List<ReviewReported> reviewReportedList = new ArrayList<>();

    protected Review() {
    }

    @Builder
    public Review(Long id, int report_count, int recommendation_count, String content, Member member, Movie movie) {
        this.id = id;
        this.report_count = report_count;
        this.recommendation_count = recommendation_count;
        this.content = content;
        this.member = member;
        this.movie = movie;
    }

    public static void setMember(Review review, Member member) {
        if (review.member != null) {
            review.member.getReviewList().remove(review);
        }
        review.member = member;
        member.getReviewList().add(review);
    }

    public static void setMovie(Review review, Movie movie) {
        if (review.movie != null) {
            review.movie.getReviewList().remove(review);
        }
        review.movie = movie;
        movie.getReviewList().add(review);
    }

}
