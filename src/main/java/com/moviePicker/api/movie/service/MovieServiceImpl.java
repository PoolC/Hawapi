package com.moviePicker.api.movie.service;

import com.moviePicker.api.auth.exception.UnauthenticatedException;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.member.service.MemberService;
import com.moviePicker.api.auth.exception.UnauthenticatedException;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.movieWatched.domain.MovieWatched;
import com.moviePicker.api.movieWatched.repository.MovieWatchedRepository;
import com.moviePicker.api.movieWished.domain.MovieWished;
import com.moviePicker.api.movieWished.repository.MovieWishedRepository;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.repository.ReviewRepository;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.movieWatched.domain.MovieWatched;
import com.moviePicker.api.movieWatched.repository.MovieWatchedRepository;
import com.moviePicker.api.movieWished.domain.MovieWished;
import com.moviePicker.api.movieWished.repository.MovieWishedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.moviePicker.api.movie.infra.MovieProvider.boxOfficeMovieCodes;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MemberService memberService;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
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

    private void checkValidPageNumber(int pageNumber, int totalPages) {
        totalPages -= 1;
        if (pageNumber > totalPages || pageNumber < 0) {
            throw new NoSuchElementException("잘못된 페이지 번호입니다.");
        }
    }

    private void checkMovieCodeExist(String movieCode){
        if(!movieRepository.existsByMovieCode(movieCode)){
            throw new IllegalArgumentException("해당 movieId 존재하지 않습니다 "+movieCode);
        }
    }

}
