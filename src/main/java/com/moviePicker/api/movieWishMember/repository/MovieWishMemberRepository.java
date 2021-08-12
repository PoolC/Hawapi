package com.moviePicker.api.movieWishMember.repository;

import com.moviePicker.api.movieWishMember.domain.MovieWishMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieWishMemberRepository extends JpaRepository<MovieWishMember,Long> {
}
