package com.moviePicker.api.movie;


import com.moviePicker.api.AcceptanceTest;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.repository.MovieRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@Component
@ActiveProfiles("movieAcceptanceDataLoader")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class MovieAcceptanceTest extends AcceptanceTest {


    @Autowired
    MovieRepository movieRepository;

    @Test
    public void 영화야들어가() throws Exception {

        List<Movie> movie = movieRepository.findAll();
        System.out.println("movie.size() = " + movie.size());


    }


}
