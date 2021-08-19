package com.moviePicker.api.movie.repository;

import com.moviePicker.api.movie.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {


    Page<Movie> findAllByMovieCode(Iterable<String> strings, Pageable pageable);

    Page<Movie> findByTitleContaining(String query, Pageable pageable);

    Page<Movie> findByTitleOrActorsContaining(String query, String query1, Pageable pageable);


}
