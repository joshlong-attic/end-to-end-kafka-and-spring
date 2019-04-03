package com.example.basics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.util.Map;
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
		this.template = buildKafkaTemplate( );
	}

	private <K, V> KafkaTemplate<K, V> buildKafkaTemplate() {

		var properties = Map.<String, Object>of(
			JsonDeserializer.TRUSTED_PACKAGES , "*",
			CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
			ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName(),
			ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName()
		);
		return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties)) {
			{
				setMessageConverter(new MessagingMessageConverter());
			}
		};
	}

	private Stream<Movie> load(Resource resource) throws Exception {
		return Files.readAllLines(resource.getFile().toPath()).stream()
			.filter(str -> !StringUtils.isEmpty(str))
			.map(this::movieFromJson);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void load() throws Exception {
		load(this.resource)
			.forEach(movies -> this.template.send("movies", movies.getMovieId(), movies));
	}

	@SneakyThrows
	private Movie movieFromJson(String json) {
		return objectMapper.readValue(json, Movie.class);
	}
}

@Log4j2
@Component
class Consumer {

	@KafkaListener(topics = "movies")
	public void onNewMovie(Movie movie) {
		log.info("movie: " + movie);
	}
}