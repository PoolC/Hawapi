package com.moviePicker.api.review.domain;


import com.moviePicker.api.comment.domain.Comment;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.reviewRecommendMember.domain.ReviewRecommendMember;
import com.moviePicker.api.reviewReportMember.domain.ReviewReportMember;
import lombok.Getter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name="reviews")
@Getter
public class Review {

    @Id @GeneratedValue
    @Column(name="id")
    private Long id;

    @Column(name = "report_count")
    private int report_count;

    @Column(name = "recommendation_count")
    private int recommendation_count;

    @Column(name="content", columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne(fetch= LAZY)
    @JoinColumn(name = "member_id", nullable = false, referencedColumnName="id")
    private Member member;

    @ManyToOne(fetch= LAZY)
    @JoinColumn(name = "movie_id", nullable = false, referencedColumnName="id")
    private Movie movie;

    @OneToMany(mappedBy="review")
    List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy="review")
    List<ReviewRecommendMember> reviewRecommendMemberList = new ArrayList<>();

    @OneToMany(mappedBy="review")
    List<ReviewReportMember> reviewReportMemberList = new ArrayList<>();

}
