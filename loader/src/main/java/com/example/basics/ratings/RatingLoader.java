package com.example.basics.ratings;


import com.example.basics.RatingLoadEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
class RatingLoader {

	private final RatingService ratingService;
	private final MessageChannel channel;
	private final AtomicBoolean load = new AtomicBoolean(false);

	RatingLoader(RatingService ratingService,
														MessageChannel ratingsChannel) {
		this.ratingService = ratingService;
		this.channel = ratingsChannel;
	}

	@EventListener(RatingLoadEvent.class)
	public void onLoadEvent() {
		this.load.set(!this.load.get());
	}

	@Scheduled(fixedRate = 100)
	public void load() throws Exception {
		if (!load.get()) {
			return;
		}
		var rating = this.ratingService.generateRandomRating(2);
		var msg = MessageBuilder.withPayload(rating)
			.setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.setHeader(KafkaHeaders.MESSAGE_KEY, rating.getId())
			.build();
		channel.send(msg);
	}
}


