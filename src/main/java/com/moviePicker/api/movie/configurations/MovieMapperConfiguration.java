package com.moviePicker.api.movie.configurations;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel;

@Configuration
public class MovieMapperConfiguration {

    private final ModelMapper modelMapper = new ModelMapper();

    @Bean
    public ModelMapper movieMapper() {

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setMethodAccessLevel(AccessLevel.PROTECTED)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE);


        return modelMapper;
    }


}
