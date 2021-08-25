package com.moviePicker.api.comment.domain;

import com.moviePicker.api.common.domain.TimestampEntity;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.review.domain.Review;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "comment")
@Getter
@Setter
public class Comment extends TimestampEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id", nullable = false, referencedColumnName = "id")
    private Review review;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false, referencedColumnName = "id")
    private Member member;

    protected Comment() {
    }

    private Comment(String content, Review review, Member member) {
        this.content = content;
        this.review = review;
        this.member = member;
        member.getCommentList().add(this);
        review.getCommentList().add(this);
    }

    public static Comment of(String content, Review review, Member member) {
        return new Comment(content, review, member);
    }
}
