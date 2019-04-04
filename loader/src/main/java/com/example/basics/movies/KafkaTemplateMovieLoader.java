package com.example.basics.movies;

import com.example.basics.KafkaUtils;
import com.example.basics.LoadEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;

class KafkaTemplateMovieLoader {

	private final Resource resource;
	private final KafkaTemplate<byte[], byte[]> template;
	private final MovieReader reader;
	private final ObjectMapper objectMapper;

	KafkaTemplateMovieLoader(@Value("classpath:/movies.json") Resource resource, MovieReader reader, ObjectMapper objectMapper) {
		this.reader = reader;
		this.resource = resource;
		this.objectMapper = objectMapper;
		this.template = KafkaUtils.buildKafkaTemplate(new String[]{"localhost:9092"});
	}

	@EventListener(LoadEvent.class)
	public void load() {
		var stream = reader.readJson(this.resource);
		stream.forEach(movie -> this.template.send("movies", Integer.toString(movie.getMovieId()).getBytes(), toJson(movie).getBytes()));
	}

	@SneakyThrows
	private String toJson(Object o) {
		return objectMapper.writeValueAsString(o);
	}
}


