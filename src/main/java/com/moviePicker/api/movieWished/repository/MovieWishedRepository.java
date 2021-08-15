package com.moviePicker.api.movieWished.repository;

import com.moviePicker.api.movieWished.domain.MovieWished;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieWishedRepository extends JpaRepository<MovieWished, Long> {
}
