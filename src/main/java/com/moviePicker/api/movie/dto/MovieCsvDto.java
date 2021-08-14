package com.moviePicker.api.movie.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieCsvDto {

    @CsvBindByName
    private Long id;

    @CsvBindByName
    private String movieCode;

    @CsvBindByName
    private String title;

    @CsvBindByName
    private String subtitle;

    @CsvBindByName
    private String score;

    @CsvBindByName
    private String genre;

    @CsvBindByName
    private String country;

    @CsvBindByName
    private String runningTime;

    @CsvBindByName
    private String pubDate;

    @CsvBindByName
    private String plot;

    @CsvBindByName
    private String filmRating;

    @CsvBindByName
    private String director;

    @CsvBindByName
    private String actors;

    @CsvBindByName
    private String poster;

    @CsvBindByName
    private String stillCuts;


}
