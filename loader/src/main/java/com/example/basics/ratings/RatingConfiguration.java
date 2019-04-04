package com.example.basics.ratings;

import com.example.basics.LoaderBindings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
class RatingConfiguration {

	@Bean
	RatingListener ratingListener() {
		return new RatingListener();
	}

	@Bean
	RatingLoader ratingLoader(
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
