package com.example.basics;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableBinding(Source.class)
class SpringCloudStreamMovieLoader {

	private final MessageChannel channel;

	SpringCloudStreamMovieLoader(Source source) {
		this.channel = source.output();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void load() throws Exception {


	}

}
