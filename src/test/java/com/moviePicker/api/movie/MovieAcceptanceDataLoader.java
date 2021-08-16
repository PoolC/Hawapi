package com.moviePicker.api.movie;


import com.moviePicker.api.movieWatched.domain.MovieWatched;
import com.moviePicker.api.movieWatched.repository.MovieWatchedRepository;
import com.moviePicker.api.movieWished.domain.MovieWished;
import com.moviePicker.api.movieWished.repository.MovieWishedRepository;
import com.moviePicker.api.auth.infra.PasswordHashProvider;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.domain.MemberRole;
import com.moviePicker.api.member.domain.MemberRoles;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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

    public final static String defaultEmail = "defaultEmail@gmail.com",
            defaultNickname = "존재하는닉네임",
            defaultPassword = "password123!",
            specificMovieCode="specificMoviecode";

    public final static Long specificReviewId=11L;

    public final static List<String> boxOfficeMovieCode= new ArrayList<>();
    public final List<Movie> wishMovies =new ArrayList<>();
    public final List<Movie> watchMovies =new ArrayList<>();
    public final List<MovieWished> movieWishedList=new ArrayList<>();
    public final List<MovieWatched> movieWatchedList=new ArrayList<>();

    @Override
    public void run(String... args) throws Exception {

        generateBoxOfficeData();

        generateSpecificMovieReviewData();

        Member default_member=Member.builder()
                .UUID(UUID.randomUUID().toString())
                .email(defaultEmail)
                .nickname(defaultNickname)
                .passwordHash(passwordHashProvider.encodePassword(defaultPassword))
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .authorizationToken(null)
                .authorizationTokenValidUntil(null)
                .reportCount(0)
                .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                .build();

        memberRepository.save(default_member);

        generateWishWatchData(default_member);

    }

    private void generateSpecificMovieReviewData() {

        Movie specificMovie=Movie.builder()
                .movieCode(specificMovieCode)
                .build();
        Review specificReview = Review.builder()
                .id(specificReviewId)
                .build();

        Review.setMovie(specificReview,specificMovie);
        movieRepository.save(specificMovie);
        reviewRepository.save(specificReview);
    }

    private void generateWishWatchData(Member default_member) {

        for(int i=0;i<31;++i){
            wishMovies.add(Movie.builder()
                .movieCode("wishMovieCode"+Integer.toString(i))
                .build());
            movieWishedList.add(MovieWished.createMovieWished(default_member,wishMovies.get(i)));
        }
        for(int i=0;i<31;++i){
            watchMovies.add(Movie.builder()
                    .movieCode("watchedMovieCode"+Integer.toString(i))
                    .build());
            movieWatchedList.add(MovieWatched.createMovieWatched(default_member,watchMovies.get(i)));
        }
        for(int i=0;i<31;++i){
            movieRepository.save(wishMovies.get(i));
            movieRepository.save(watchMovies.get(i));
            movieWishedRepository.save(movieWishedList.get(i));
            movieWatchedRepository.save(movieWatchedList.get(i));
        }

    }

    private void generateBoxOfficeData(){

        for(int i=0;i<31;++i){
            this.boxOfficeMovieCode.add("boxOfficeCode"+Integer.toString(i));
            movieRepository.save(Movie.builder()
                    .movieCode("boxOfficeCode"+Integer.toString(i))
                    .build());
        }
    }

}
