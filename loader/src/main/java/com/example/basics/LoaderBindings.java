package com.example.basics;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface LoaderBindings {

	String RATINGS = "ratings";
	String MOVIES = "movies";

	@Output(MOVIES)
	MessageChannel movies();

	@Output(RATINGS)
	MessageChannel ratings();
}
