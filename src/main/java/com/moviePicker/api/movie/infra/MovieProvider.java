package com.moviePicker.api.movie.infra;

import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.dto.CsvMovieData;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class MovieProvider implements ApplicationRunner {

    public static ArrayList<String> boxOfficeMovieCodes;

    private final MovieRepository movieRepository;
    private final MovieCrawler movieCrawler;
    private final ModelMapper movieMapper;

    public final String updateDataFilePath = "data/updateData.csv";

    @Override
    public void run(ApplicationArguments args) throws Exception {

        updateBoxOfficeData();
        List<Movie> movie = movieRepository.findAll();
        System.out.println("movie.size() = " + movie.size());

    }


    public void updateBoxOfficeData() {
        boxOfficeMovieCodes = movieCrawler.crawlBoxOfficeMovieCodes();
        List<String> updatingMovieCodes = boxOfficeMovieCodes.stream()
                .filter(movieCode -> (movieRepository.findById(movieCode).isEmpty()))
                .collect(Collectors.toList());


        movieCrawler.writeMovieDataToCsv(movieCrawler.crawlMovieData((ArrayList<String>) updatingMovieCodes), updateDataFilePath);
        readCsvAndSave(updateDataFilePath);


    }

    public void readCsvAndSave(String filename) {
        ClassPathResource resource = new ClassPathResource(filename);

        try {
            Path path = Paths.get(resource.getURI());

            List<CsvMovieData> csvMovieDataList = new CsvToBeanBuilder<CsvMovieData>(new BufferedReader(new InputStreamReader(new FileInputStream(path.toString()), "EUC-KR")))
                    .withType(CsvMovieData.class)
                    .build()
                    .parse();
            List<Movie> movieEntityList = csvMovieDataList.stream()
                    .map(dto -> (movieMapper.map(dto, Movie.class)))
                    .collect(Collectors.toList());
            movieEntityList.forEach(movieRepository::save);


        } catch (Exception e) {
            e.getStackTrace();
        }

    }


}
