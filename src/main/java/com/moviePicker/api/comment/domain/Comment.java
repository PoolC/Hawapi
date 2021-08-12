package com.moviePicker.api.comment.domain;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.review.domain.Review;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name="comment")
@Getter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @Column(name="content")
    private String content;

    @ManyToOne(fetch= LAZY)
    @JoinColumn(name = "review_id", nullable = false, referencedColumnName="id")
    private Review review;

    @ManyToOne(fetch= LAZY)
    @JoinColumn(name = "member_id", nullable = false, referencedColumnName="id")
    private Member member;
}
