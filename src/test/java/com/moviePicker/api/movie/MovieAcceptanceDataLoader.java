package com.moviePicker.api.movie;


import com.moviePicker.api.auth.infra.PasswordHashProvider;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.domain.MemberRole;
import com.moviePicker.api.member.domain.MemberRoles;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.movie.domain.MovieWatched;
import com.moviePicker.api.movie.repository.MovieWatchedRepository;
import com.moviePicker.api.movie.domain.MovieWished;
import com.moviePicker.api.movie.repository.MovieWishedRepository;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
@Profile("movieAcceptanceDataLoader")
@RequiredArgsConstructor
public class MovieAcceptanceDataLoader implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final MemberRepository memberRepository;
    private final MovieWishedRepository movieWishedRepository;
    private final MovieWatchedRepository movieWatchedRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordHashProvider passwordHashProvider;


    private final static String testMovieDataFile = "data/sample.csv";

    public final static String memberEmail = "defaultEmail@gmail.com",
            memberNickname = "존재하는닉네임",
            memberPassword = "password123!",
            specificMovieCode = "specificMovieCode";

    public final static Long specificReviewId = 1L;

    public final static List<String> boxOfficeMovieCode = new ArrayList<>();
    public final static List<Movie> wishMovies = new ArrayList<>();
    public final static List<Movie> watchMovies = new ArrayList<>();
    public final static List<MovieWished> movieWishedList = new ArrayList<>();
    public final static List<MovieWatched> movieWatchedList = new ArrayList<>();

    public final static Integer totalMoviesSize = 31;
    public final static Integer sizeOfPage = 10;

    public static Member defaultMember;
    public static Movie specificMovie;
    public static Review specificReview;

    @Override
    public void run(String... args) throws Exception {

        generateDefaultMember();

        generateBoxOfficeMovies();

        generateSpecificMovieAndReview();

        generateWishWatchMovie();

    }

    private void generateDefaultMember() {
        defaultMember = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .email(memberEmail)
                .nickname(memberNickname)
                .passwordHash(passwordHashProvider.encodePassword(memberPassword))
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .authorizationToken(null)
                .authorizationTokenValidUntil(null)
                .reportCount(0)
                .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                .build();

        memberRepository.save(defaultMember);
    }

    private void generateBoxOfficeMovies() {
        for (int i = 0; i < totalMoviesSize; ++i) {
            this.boxOfficeMovieCode.add("boxOfficeCode" + i);
            movieRepository.save(Movie.builder()
                    .movieCode("boxOfficeCode" + i)
                    .build());
        }
    }

    @Transactional
    private void generateSpecificMovieAndReview() {
        specificMovie = Movie.builder()
                .movieCode(specificMovieCode)
                .title("specificMovie")
                .build();
        specificReview = Review.builder()
                .id(specificReviewId)
                .build();

        specificReview.setMovie(specificMovie);
        specificReview.setMember(defaultMember);

        movieRepository.save(specificMovie);
        reviewRepository.save(specificReview);

//        Review.setMovie(specificReview, specificMovie);
//        Review.setMember(specificReview, defaultMember);


    }

    private void generateWishWatchMovie() {
        for (int i = 0; i < totalMoviesSize; ++i) {
            wishMovies.add(Movie.builder()
                    .movieCode("wishMovieCode" + Integer.toString(i))
                    .build());
            movieWishedList.add(MovieWished.createMovieWished(defaultMember, wishMovies.get(i)));
        }
        for (int i = 0; i < totalMoviesSize; ++i) {
            watchMovies.add(Movie.builder()
                    .movieCode("watchedMovieCode" + Integer.toString(i))
                    .build());
            movieWatchedList.add(MovieWatched.createMovieWatched(defaultMember, watchMovies.get(i)));
        }
        for (int i = 0; i < totalMoviesSize; ++i) {
            movieRepository.save(wishMovies.get(i));
            movieRepository.save(watchMovies.get(i));
            movieWishedRepository.save(movieWishedList.get(i));
            movieWatchedRepository.save(movieWatchedList.get(i));
        }
    }


}
