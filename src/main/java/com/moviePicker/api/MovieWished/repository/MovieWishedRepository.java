package com.moviePicker.api.MovieWished.repository;

import com.moviePicker.api.MovieWished.domain.MovieWished;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieWishedRepository extends JpaRepository<MovieWished, Long> {
}
