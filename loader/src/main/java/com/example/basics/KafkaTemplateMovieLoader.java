package com.example.basics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
class KafkaTemplateMovieLoader {

	private final Resource resource;
	private final KafkaTemplate<Integer, Movie> template;
	private final MovieReader reader;

	KafkaTemplateMovieLoader(@Value("classpath:/movies-json.js") Resource resource, MovieReader reader) {
		this.reader = reader;
		this.resource = resource;
		this.template = KafkaUtils.buildKafkaTemplate(new String[]{"localhost:9092"});
	}

	@EventListener(ApplicationReadyEvent.class)
	public void load() throws Exception {
		var stream = reader.readJson(this.resource);
		stream.forEach(movie -> this.template.send("movies", movie.getMovieId(), movie));
	}

}


