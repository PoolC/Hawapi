package com.moviePicker.api.movieWatched.repository;

import com.moviePicker.api.movieWatched.domain.MovieWatched;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieWatchedRepository extends JpaRepository<MovieWatched, Long> {
}
