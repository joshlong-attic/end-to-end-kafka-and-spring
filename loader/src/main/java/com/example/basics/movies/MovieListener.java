package com.example.basics.movies;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;

@Log4j2
class MovieListener {

	@KafkaListener(topics = "movies")
	public void logNewMovieMessage(Object o) {
		log.info("movie: " + o);
	}

}
