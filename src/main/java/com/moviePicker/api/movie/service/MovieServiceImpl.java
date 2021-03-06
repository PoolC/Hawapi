package com.moviePicker.api.movie.service;


import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.service.MemberService;
import com.moviePicker.api.movie.domain.BoxOfficeMovie;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.domain.MovieWatched;
import com.moviePicker.api.movie.domain.MovieWished;
import com.moviePicker.api.movie.repository.BoxOfficeMovieRepository;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.movie.repository.MovieWatchedRepository;
import com.moviePicker.api.movie.repository.MovieWishedRepository;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MemberService memberService;
    private final MovieRepository movieRepository;
    private final BoxOfficeMovieRepository boxOfficeMovieRepository;
    private final ReviewRepository reviewRepository;
    private final MovieWishedRepository movieWishedRepository;
    private final MovieWatchedRepository movieWatchedRepository;

    @Override
    public List<BoxOfficeMovie> searchMoviesRunning(Pageable pageable) {

        Page<BoxOfficeMovie> boxOfficeMovies = boxOfficeMovieRepository.findAll(pageable);
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

        Page<MovieWished> movieWisheds = movieWishedRepository.findMovieWishedsByMember(member, pageable);
        checkValidPageNumber(pageable.getPageNumber(), movieWisheds.getTotalPages());
        return movieWisheds.getContent();

    }

    @Override
    public List<MovieWatched> searchMoviesWatched(Member member, Pageable pageable) {

        Page<MovieWatched> movieWatcheds = movieWatchedRepository.findMovieWatchedsByMember(member, pageable);
        checkValidPageNumber(pageable.getPageNumber(), movieWatcheds.getTotalPages());
        return movieWatcheds.getContent();

    }

    @Override
    public Movie searchMovieByMovieCode(String movieCode) {
        return movieRepository.findByMovieCode(movieCode)
                .orElseThrow(() ->
                        new NoSuchElementException("???????????? ?????? movieId ????????? " + movieCode));
    }

    @Override
    public Movie searchMovieByReviewId(Long reviewId) {
        //reviewService ?????? ???????????? ?????????
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new IllegalArgumentException(("???????????? ?????? reviewId ????????? " + reviewId)));
        List<Review> all = reviewRepository.findAll();

        return review.getMovie();
    }

    @Override
    @Transactional
    public boolean registerMovieWished(Member member, String movieCode) {

        checkMovieCodeExist(movieCode);
        Member registerMember = memberService.getMemberByEmail(member.getEmail());
        Movie registerMovie = searchMovieByMovieCode(movieCode);
        if (movieWishedRepository.findByMemberAndMovie(registerMember, registerMovie).isPresent()) {
            movieWishedRepository.removeByMemberAndMovie(registerMember, registerMovie);
            return false;
        } else {
            movieWishedRepository.save(MovieWished.of(registerMember, registerMovie));
            return true;
        }
    }

    @Override
    @Transactional
    public boolean registerMovieWatched(Member member, String movieCode) {

        checkMovieCodeExist(movieCode);
        Member registerMember = memberService.getMemberByEmail(member.getEmail());
        Movie registerMovie = searchMovieByMovieCode(movieCode);
        if (movieWatchedRepository.findByMemberAndMovie(registerMember, registerMovie).isPresent()) {
            movieWatchedRepository.removeByMemberAndMovie(registerMember, registerMovie);
            return false;
        } else {
            movieWatchedRepository.save(MovieWatched.of(registerMember, registerMovie));
            return true;
        }
    }


    private void checkValidPageNumber(int pageNumber, int totalPages) {
        totalPages -= 1;
        if (pageNumber > totalPages || pageNumber < 0) {
            throw new NoSuchElementException("????????? ????????? ???????????????.");
        }
    }

    private void checkMovieCodeExist(String movieCode) {
        if (!movieRepository.existsByMovieCode(movieCode)) {
            throw new IllegalArgumentException("?????? movieId ???????????? ???????????? " + movieCode);
        }
    }

}
