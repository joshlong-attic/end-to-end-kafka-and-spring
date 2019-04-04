package com.example.basics.ratings;

import com.example.basics.LoaderBindings;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Log4j2
@Configuration
class RatingConfiguration {

	@Bean
	RatingListener ratingListener() {
		return new RatingListener();
	}

	@Bean
	RatingLoader ratingLoader(ObjectMapper objectMapper,
																											RatingService ratingService,
																											LoaderBindings bindings) {
		return new RatingLoader(ratingService, bindings.ratings());
	}

	@Bean
	RatingService ratingService(
		ObjectMapper objectMapper,
		@Value("classpath:/ratings.json") Resource json) throws Exception {
		return new RatingService(objectMapper, json);
	}
}
