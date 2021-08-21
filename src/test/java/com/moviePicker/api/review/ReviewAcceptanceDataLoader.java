package com.moviePicker.api.review;

import com.moviePicker.api.auth.infra.PasswordHashProvider;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.domain.MemberRole;
import com.moviePicker.api.member.domain.MemberRoles;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.review.repository.CommentRepository;
import com.moviePicker.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("reviewAcceptanceDataLoader")
@RequiredArgsConstructor
public class ReviewAcceptanceDataLoader implements ApplicationRunner {
    private final MovieRepository movieRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final PasswordHashProvider passwordHashProvider;


    public final static String memberEmail = "defaultEmail@gmail.com",
            memberNickname = "존재하는닉네임",
            memberPassword = "password123!",
            specificMovieCode = "specificMovieCode";


    public static Member defaultMember;

    @Override
    public void run(ApplicationArguments args) throws Exception {

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

}
