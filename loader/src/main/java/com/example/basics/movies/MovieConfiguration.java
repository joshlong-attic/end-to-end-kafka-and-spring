package com.example.basics.movies;

import com.example.basics.LoaderBindings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
class MovieConfiguration {

	private final static String MOVIES_JSON = "classpath:/movies.json";

	@Bean
	MovieListener movieListener() {
		return new MovieListener();
	}

	@Bean
	MovieReader movieReader(ObjectMapper objectMapper) {
		return new MovieReader(objectMapper);
	}

	//	@Bean
	KafkaTemplateMovieLoader templateMovieLoader(
		ObjectMapper om,
		@Value(MOVIES_JSON) Resource json,
		MovieReader reader) {
		return new KafkaTemplateMovieLoader(json, reader, om);
	}

	@Bean
	SpringCloudStreamMovieLoader streamMovieLoader(
		LoaderBindings bindings,
		ApplicationEventPublisher publisher,
		MovieReader reader,
		ObjectMapper om,
		@Value(MOVIES_JSON) Resource json) {
		return new SpringCloudStreamMovieLoader(bindings, reader, json, publisher, om);
	}
}
