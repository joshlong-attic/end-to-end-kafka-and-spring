package com.example.basics;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
class MovieListener {

	@KafkaListener(topics = "movies")
	public void logNewMovieMessage(ConsumerRecord<Integer, Movie> movieConsumerRecord) {
		var id = movieConsumerRecord.key();
		var movie = movieConsumerRecord.value();
		log.info(String.format("movie with key %s and value %s", id, movie));
	}

}
