package com.moviePicker.api.reviewReported.domain;

import com.moviePicker.api.common.domain.TimestampEntity;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.review.domain.Review;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "report")
@Getter
public class ReviewReported extends TimestampEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false, referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id", nullable = false, referencedColumnName = "id")
    private Review review;

    protected ReviewReported() {
    }
}
