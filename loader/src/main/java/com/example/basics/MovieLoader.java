package com.example.basics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.util.stream.Stream;

@Component
@Log4j2
class MovieLoader {

	private final KafkaTemplate<Integer, Movie> template;
	private final ObjectMapper objectMapper;
	private final Resource resource;

	MovieLoader(ObjectMapper objectMapper,
													@Value("classpath:/movies-json.js") Resource resource) {
		this.objectMapper = objectMapper;
		this.resource = resource;
		this.template = KafkaUtils.buildKafkaTemplate(
			new String[]{"localhost:9092"});
	}

	@EventListener(ApplicationReadyEvent.class)
	public void load() throws Exception {
		this.load(this.resource)
			.forEach(movie -> this.template.send("movies", movie.getMovieId(), movie));
	}

	@SneakyThrows
	private Movie movieFromJson(String json) {
		return objectMapper.readValue(json, Movie.class);
	}

	private Stream<Movie> load(Resource resource) throws Exception {
		var path = resource.getFile().toPath();
		return Files
			.readAllLines(path)
			.stream()
			.filter(str -> !StringUtils.isEmpty(str))
			.map(this::movieFromJson);
	}
}

@Log4j2
@Component
//@ConditionalOnProperty("debug")
class MovieListener {

	@KafkaListener(topics = "movies")
	public void logNewMovieMessage(ConsumerRecord<Integer, Movie> movieConsumerRecord) {
		var id = movieConsumerRecord.key();
		var movie = movieConsumerRecord.value();
		log.info(String.format("movie with key %s and value %s", id, movie));
	}

}