package com.moviePicker.api.movie.repository;

import com.moviePicker.api.movie.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Long> {
}
