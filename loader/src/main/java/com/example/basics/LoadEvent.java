package com.example.basics;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class LoadEvent extends ApplicationEvent {

	LoadEvent() {
		super(UUID.randomUUID().toString());
	}
}
