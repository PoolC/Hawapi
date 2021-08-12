package com.moviePicker.api.movieWatchMember.repository;

import com.moviePicker.api.movieWatchMember.domain.MovieWatchMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieWatchMemberRepository extends JpaRepository<MovieWatchMember,Long> {
}
