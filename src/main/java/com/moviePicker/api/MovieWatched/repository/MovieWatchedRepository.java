package com.moviePicker.api.MovieWatched.repository;

import com.moviePicker.api.MovieWatched.domain.MovieWatched;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieWatchedRepository extends JpaRepository<MovieWatched, Long> {
}
