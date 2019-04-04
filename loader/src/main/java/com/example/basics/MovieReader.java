package com.example.basics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.util.stream.Stream;

@Component
class MovieReader {

	private final ObjectMapper objectMapper;

	MovieReader(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	Stream<Movie> readJson(Resource resource) throws Exception {
		var path = resource.getFile().toPath();
		return Files
			.readAllLines(path)
			.stream()
			.filter(str -> !StringUtils.isEmpty(str))
			.map(this::movieFromJson);
	}

	@SneakyThrows
	private Movie movieFromJson(String json) {
		return objectMapper.readValue(json, Movie.class);
	}

}
