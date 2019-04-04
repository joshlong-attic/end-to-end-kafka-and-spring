package com.example.basics.ratings;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.annotation.StreamListener;

@Log4j2
class RatingListener {

	@StreamListener("ratings")
	public void logNewRatingMessage(Rating rating) {
		log.info("new rating: " + rating);
	}
}
