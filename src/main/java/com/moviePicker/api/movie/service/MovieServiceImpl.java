package com.moviePicker.api.movie.service;

import com.moviePicker.api.auth.exception.UnauthenticatedException;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.movieWatched.domain.MovieWatched;
import com.moviePicker.api.movieWatched.repository.MovieWatchedRepository;
import com.moviePicker.api.movieWished.domain.MovieWished;
import com.moviePicker.api.movieWished.repository.MovieWishedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.moviePicker.api.movie.infra.MovieProvider.boxOfficeMovieCodes;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieWishedRepository movieWishedRepository;
    private final MovieWatchedRepository movieWatchedRepository;

    @Override
    public List<Movie> searchMoviesRunning(Pageable pageable) {

        Page<Movie> boxOfficeMovies = movieRepository.findAllByMovieCode(boxOfficeMovieCodes, pageable);
        checkValidPageNumber(pageable.getPageNumber(), boxOfficeMovies.getTotalPages());


        return boxOfficeMovies.getContent();
    }

    @Override
    public List<Movie> searchMoviesByQuery(String query, Pageable pageable) {
        Page<Movie> queryMovies = movieRepository.findByTitleOrActorsContaining(query, query, pageable);

        checkValidPageNumber(pageable.getPageNumber(), queryMovies.getTotalPages());
        return queryMovies.getContent();
    }

    @Override
    public List<MovieWished> searchMoviesWished(Member member, Pageable pageable) {
        checkIsLogin(member);
        Page<MovieWished> movieWisheds = movieWishedRepository.findMovieWishedsByMember(member, pageable);
        checkValidPageNumber(pageable.getPageNumber(), movieWisheds.getTotalPages());
        return movieWisheds.getContent();

    }

    @Override
    public List<MovieWatched> searchMoviesWatched(Member member, Pageable pageable) {
        checkIsLogin(member);
        Page<MovieWatched> movieWatcheds = movieWatchedRepository.findMovieWatchedsByMember(member, pageable);
        checkValidPageNumber(pageable.getPageNumber(), movieWatcheds.getTotalPages());
        return movieWatcheds.getContent();

    }

    @Override
    public Movie searchMovieByMovieId() {
        return null;
    }

    @Override
    public Movie searchMovieByReviewId() {
        return null;
    }

    @Override
    public boolean registerMovieWished() {
        return false;
    }

    @Override
    public boolean registerMovieWatched() {
        return false;
    }

    private void checkIsLogin(Member member) {
        Optional.ofNullable(member)
                .orElseThrow(() -> {
                    throw new UnauthenticatedException("로그인 해주세요");
                });
    }

    private void checkValidPageNumber(int pageNumber, int totalPages) {
        totalPages -= 1;
        if (pageNumber > totalPages || pageNumber < 0) {
            throw new NoSuchElementException("잘못된 페이지 번호입니다.");
        }
    }

}
