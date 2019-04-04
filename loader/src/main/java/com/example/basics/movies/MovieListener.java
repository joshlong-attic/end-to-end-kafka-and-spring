package com.example.basics.movies;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.annotation.StreamListener;

@Log4j2
class MovieListener {

	@StreamListener("movies")
	public void logNewMovieMessage(Movie movie) {
		log.info("new movie " + movie.toString());
	}
}
