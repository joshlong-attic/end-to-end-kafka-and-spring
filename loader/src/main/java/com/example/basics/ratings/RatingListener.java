package com.example.basics.ratings;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

@Log4j2
class RatingListener {

	@KafkaListener(topics = "ratings")
	public void logNewRatingMessage(ConsumerRecord<byte[], byte[]> rating) {
		var key = new String(rating.key());
		var value = new String(rating.value());
		log.info("rating key: " + key);
		log.info("rating value: " + value);
	}
}
