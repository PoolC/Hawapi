package com.moviePicker.api.movie.service;

import com.moviePicker.api.auth.exception.UnauthenticatedException;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.member.service.MemberService;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.movieWatched.domain.MovieWatched;
import com.moviePicker.api.movieWatched.repository.MovieWatchedRepository;
import com.moviePicker.api.movieWished.domain.MovieWished;
import com.moviePicker.api.movieWished.repository.MovieWishedRepository;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MemberService memberService;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final MovieWishedRepository movieWishedRepository;
    private final MovieWatchedRepository movieWatchedRepository;

    @Override
    public List<Movie> searchMoviesRunning() {
        return null;
    }

    @Override
    public List<Movie> searchMoviesByQuery() {
        return null;
    }

    @Override
    public List<Movie> searchMoviesWished() {
        return null;
    }

    @Override
    public List<Movie> searchMoviesWatched() {
        return null;
    }

    @Override
    public Movie searchMovieByMovieCode(String movieCode) {
        return movieRepository.findByMovieCode(movieCode)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 movieId 입니다 "+movieCode));
    }

    @Override
    public Movie searchMovieByReviewId(Long reviewId) {
        //reviewService 에서 작성해야 될수도
        Review review=reviewRepository.findById(reviewId)
                .orElseThrow(()->
                        new IllegalArgumentException(("존재하지 않는 reviewId 입니다 "+reviewId)));
        return review.getMovie();
    }

    @Override
    @Transactional
    public boolean registerMovieWished(Member member, String movieCode) {
        checkIsLogin(member);
        checkMovieCodeExist(movieCode);
        Member registerMember=memberService.getMemberByEmail(member.getEmail());
        Movie registerMovie=searchMovieByMovieCode(movieCode);
        if(movieWishedRepository.findByMemberAndMovie(registerMember,registerMovie).isPresent()){
            movieWishedRepository.removeByMemberAndMovie(registerMember,registerMovie);
            return false;
        }
        else{
            movieWishedRepository.save(MovieWished.createMovieWished(registerMember, registerMovie));
            return true;
        }
    }

    @Override
    @Transactional
    public boolean registerMovieWatched(Member member, String movieCode) {
        checkIsLogin(member);
        checkMovieCodeExist(movieCode);
        Member registerMember=memberService.getMemberByEmail(member.getEmail());
        Movie registerMovie=searchMovieByMovieCode(movieCode);
        if(movieWatchedRepository.findByMemberAndMovie(registerMember,registerMovie).isPresent()){
            movieWatchedRepository.removeByMemberAndMovie(registerMember,registerMovie);
            return false;
        }
        else{
            movieWatchedRepository.save(MovieWatched.createMovieWatched(registerMember, registerMovie));
            return true;
        }
    }

    private void checkIsLogin(Member member) {
        Optional.ofNullable(member)
                .orElseThrow(() -> {
                    throw new UnauthenticatedException("로그인 해주세요");
                });
    }

    private void checkMovieCodeExist(String movieCode){
        if(!movieRepository.existsByMovieCode(movieCode)){
            throw new IllegalArgumentException("해당 movieId 존재하지 않습니다 "+movieCode);
        }
    }

}
