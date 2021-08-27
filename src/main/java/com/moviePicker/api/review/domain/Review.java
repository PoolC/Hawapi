package com.moviePicker.api.review.domain;


import com.moviePicker.api.comment.domain.Comment;
import com.moviePicker.api.comment.dto.CommentUpdateRequest;
import com.moviePicker.api.common.domain.TimestampEntity;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.dto.MemberUpdateRequest;
import com.moviePicker.api.movie.domain.Movie;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "reviews")
@Getter
@Setter
public class Review extends TimestampEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "report_count")
    private Integer reportCount;

    @Column(name = "recommendation_count")
    private Integer recommendationCount;

    @Column(name = "title", columnDefinition = "LONGTEXT")
    private String title;

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
    List<Recommendation> recommendationList = new ArrayList<>();

    @OneToMany(mappedBy = "review")
    List<Report> reportList = new ArrayList<>();

    protected Review() {
    }


    private Review(Member member, Movie movie, String title, String content) {
        this.member = member;
        this.movie = movie;
        this.title = title;
        this.content = content;
        this.reportCount = 0;
        this.recommendationCount = 0;

        member.getReviewList().add(this);
        movie.getReviewList().add(this);
    }

    public static Review of(Member member, Movie movie, String title, String content) {
        return new Review(member, movie, title, content);
    }

    public void addRecommendationCount() {
        recommendationCount += 1;
    }

    public void subtractRecommendationCount() {
        if (recommendationCount - 1 < 0) {
            return;
        }
        recommendationCount -= 1;
    }

    public void addReportCount() {
        reportCount += 1;
    }


    
}
