package com.moviePicker.api.review.domain;

import com.moviePicker.api.common.domain.TimestampEntity;
import com.moviePicker.api.member.domain.Member;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "recommend")
@Getter
public class Recommendation extends TimestampEntity {

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

    protected Recommendation() {
    }

    private Recommendation(Member member, Review review) {

        this.member = member;
        this.review = review;
        member.getRecommendationList().add(this);
        review.getRecommendationList().add(this);
    }

    public static Recommendation of(Member member, Review review) {
        return new Recommendation(member, review);
    }


}
