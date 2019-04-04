package com.example.basics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@EnableScheduling
@EnableBinding(LoaderBindings.class)
@SpringBootApplication
public class BasicsApplication {

	@Bean
	ScheduledExecutorService scheduledExecutorService() {
		return Executors.newSingleThreadScheduledExecutor();
	}

	public static void main(String[] args) {
		SpringApplication.run(BasicsApplication.class, args);
	}
}

@RestController
class LoadingRestController {

	private final ApplicationEventPublisher publisher;

	LoadingRestController(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@PostMapping("/load")
	public void go() {
		this.publisher.publishEvent(new MovieLoadEvent());
	}

}
