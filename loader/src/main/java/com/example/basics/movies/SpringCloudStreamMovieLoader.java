package com.example.basics.movies;

import com.example.basics.LoadEvent;
import com.example.basics.LoaderBindings;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
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
	private final ObjectMapper objectMapper;

	SpringCloudStreamMovieLoader(
		LoaderBindings source,
		MovieReader movieReader,
		Resource resource,
		ObjectMapper objectMapper) {

		this.output = source.movies();
		this.resource = resource;
		this.movieReader = movieReader;
		this.objectMapper = objectMapper;
	}

	@EventListener(LoadEvent.class)
	public void load() {
		var stream = this.movieReader.readJson(this.resource);
		Consumer<Movie> movieConsumer = (movie) -> {
			var message = messageFromMovie(movie);
			log.info("sending " + movie.toString());
			output.send(message);
		};
		stream.forEach(movieConsumer);
	}

	@SneakyThrows
	private String toJson(Movie m) {
		return objectMapper.writeValueAsString(m);
	}

	private Message<String> messageFromMovie(Movie movie) {
		return MessageBuilder
			.withPayload(toJson(movie))
			.setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.setHeader(KafkaHeaders.MESSAGE_KEY, Integer.toString(movie.getMovieId()).getBytes())
			.build();
	}
}
