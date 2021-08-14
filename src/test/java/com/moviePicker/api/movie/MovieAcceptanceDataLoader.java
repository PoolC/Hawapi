package com.moviePicker.api.movie;


import com.moviePicker.api.auth.infra.PasswordHashProvider;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.domain.MemberRole;
import com.moviePicker.api.member.domain.MemberRoles;
import com.moviePicker.api.member.repository.MemberRepository;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.dto.MovieCsvDto;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Profile("movieAcceptanceDataLoader")
@RequiredArgsConstructor
public class MovieAcceptanceDataLoader implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;
    private final ModelMapper movieMapper;

    private final static String testMovieDataFile = "sample.csv";

    public final static String defaultEmail = "defaultEmail@gmail.com",
            defaultNickname = "존재하는닉네임",
            defaultPassword = "password123!";

    @Override
    public void run(String... args) throws Exception {
        List<MovieCsvDto> movieCsvDtoList = readCsvToBean(testMovieDataFile);
        List<Movie> movieEntityList = movieCsvDtoList.stream()
                .map(dto -> (movieMapper.map(dto, Movie.class)))
                .collect(Collectors.toList());
        movieEntityList.forEach(movieRepository::save);


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

    //csv -> dto 맵핑해서 List에 담아 리턴
    public List<MovieCsvDto> readCsvToBean(String filename) {
        ClassPathResource resource = new ClassPathResource(filename);
        List<MovieCsvDto> data = null;


        try {


            data = new CsvToBeanBuilder<MovieCsvDto>(new BufferedReader(new InputStreamReader(new FileInputStream(resource.getFile()), "EUC-KR")))
                    .withType(MovieCsvDto.class)
                    .build()
                    .parse();


        } catch (Exception e) {
            e.getStackTrace();
            System.out.println("error = " + e);
        }

        return data;
    }

}
