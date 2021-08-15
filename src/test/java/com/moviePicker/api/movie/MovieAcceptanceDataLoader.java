package com.moviePicker.api.movie;


import com.moviePicker.api.auth.infra.PasswordHashProvider;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.domain.MemberRole;
import com.moviePicker.api.member.domain.MemberRoles;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.movie.infra.MovieProvider;
import com.moviePicker.api.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@Profile("movieAcceptanceDataLoader")
@RequiredArgsConstructor
public class MovieAcceptanceDataLoader implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final MovieProvider movieProvider;
    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;
    private final ModelMapper movieMapper;

    private final static String testMovieDataFile = "data/sample.csv";

    public final static String defaultEmail = "defaultEmail@gmail.com",
            defaultNickname = "존재하는닉네임",
            defaultPassword = "password123!";

    @Override
    public void run(String... args) throws Exception {

        movieProvider.readCsvAndSave("data/sample.csv");

        memberRepository.save(
                Member.builder()
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
                        .build());


    }


}
