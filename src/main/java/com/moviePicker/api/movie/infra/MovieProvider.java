package com.moviePicker.api.movie.infra;

import com.moviePicker.api.movie.domain.BoxOfficeMovie;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.dto.CsvMovieData;
import com.moviePicker.api.movie.repository.BoxOfficeMovieRepository;
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
    public final String updateDataFilePath = "data/updateData.csv";
    public final String boxOfficeDataFilePath = "data/boxOffice.csv";
    public final String sampleDataFilePath = "data/sample.csv";
    private final MovieRepository movieRepository;
    private final BoxOfficeMovieRepository boxOfficeMovieRepository;
    private final MovieCrawler movieCrawler;
    private final ModelMapper movieMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        readCsvAndSaveMovie(sampleDataFilePath);
        updateBoxOfficeData();

        List<Movie> movie = movieRepository.findAll();
        System.out.println("movie.size() = " + movie.size());

        List<BoxOfficeMovie> boxOfficeMovies = boxOfficeMovieRepository.findAll();
        System.out.println("boxOfficeMovies.size() = " + boxOfficeMovies.size());


    }


    public void updateBoxOfficeData() {
        boxOfficeMovieCodes = movieCrawler.crawlBoxOfficeMovieCodes();
        movieCrawler.writeMovieDataToCsv(movieCrawler.crawlMovieData(boxOfficeMovieCodes), boxOfficeDataFilePath);
        readCsvAndSaveBoxOfficeMovie(boxOfficeDataFilePath);


        List<String> updatingMovieCodes = boxOfficeMovieCodes.stream()
                .filter(movieCode -> (movieRepository.findById(movieCode).isEmpty()))
                .collect(Collectors.toList());


        movieCrawler.writeMovieDataToCsv(movieCrawler.crawlMovieData((ArrayList<String>) updatingMovieCodes), updateDataFilePath);
        readCsvAndSaveMovie(updateDataFilePath);


    }

    public void readCsvAndSaveMovie(String filename) {
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

            movieRepository.saveAll(movieEntityList);


        } catch (Exception e) {
            e.getStackTrace();
            e.printStackTrace();
        }

    }

    public void readCsvAndSaveBoxOfficeMovie(String filename) {
        ClassPathResource resource = new ClassPathResource(filename);

        try {
            Path path = Paths.get(resource.getURI());

            List<CsvMovieData> csvMovieDataList = new CsvToBeanBuilder<CsvMovieData>(new BufferedReader(new InputStreamReader(new FileInputStream(path.toString()), "EUC-KR")))
                    .withType(CsvMovieData.class)
                    .build()
                    .parse();
            List<BoxOfficeMovie> movieEntityList = csvMovieDataList.stream()
                    .map(dto -> (movieMapper.map(dto, BoxOfficeMovie.class)))
                    .collect(Collectors.toList());


            boxOfficeMovieRepository.saveAll(movieEntityList);


        } catch (Exception e) {
            e.getStackTrace();
            e.printStackTrace();
        }

    }


}
