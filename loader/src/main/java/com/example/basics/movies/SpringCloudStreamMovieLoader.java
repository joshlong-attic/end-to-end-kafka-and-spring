package com.example.basics.movies;

import com.example.basics.LoaderBindings;
import com.example.basics.MovieLoadEvent;
import com.example.basics.RatingLoadEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Consumer;

@Log4j2
class SpringCloudStreamMovieLoader {

	private final MovieReader movieReader;
	private final MessageChannel output;
	private final Resource resource;
	private final ApplicationEventPublisher publisher;

	SpringCloudStreamMovieLoader(
		LoaderBindings source,
		MovieReader movieReader,
		Resource resource,
		ApplicationEventPublisher publisher) {

		this.publisher = publisher;
		this.output = source.movies();
		this.resource = resource;
		this.movieReader = movieReader;
	}

	@EventListener(MovieLoadEvent.class)
	public void load() {
		var stream = this.movieReader.readJson(this.resource);
		Consumer<Movie> movieConsumer = (movie) -> {
			output.send(messageFromMovie(movie));
		};
		stream.forEach(movieConsumer);
		this.publisher.publishEvent(new RatingLoadEvent());
	}

	private Message<Movie> messageFromMovie(Movie movie) {
		return MessageBuilder
			.withPayload(movie)
			.setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.setHeader(KafkaHeaders.MESSAGE_KEY, movie.getMovieId())
			.build();
	}
}
