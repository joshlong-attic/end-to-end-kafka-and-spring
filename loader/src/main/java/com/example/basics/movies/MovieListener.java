package com.example.basics.movies;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

@Log4j2
class MovieListener {

	@KafkaListener(topics = "movies")
	public void logNewMovieMessage(ConsumerRecord<byte[], byte[]> movie) {
		var key = new String(movie.key());
		var value = new String(movie.value());
		log.info("movie key: " + key + ", value: " + value);
	}
}
