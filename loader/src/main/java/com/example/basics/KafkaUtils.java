package com.example.basics;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.StringUtils;

import java.util.Map;

public abstract class KafkaUtils {

	public static <K, V> KafkaTemplate<K, V> buildKafkaTemplate(String[] bootstrapSvrs) {
		var jsonSerializerClassName = JsonSerializer.class.getName();
		var properties = Map.<String, Object>of(
			JsonDeserializer.TRUSTED_PACKAGES, "*",
			CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, StringUtils.arrayToCommaDelimitedString(bootstrapSvrs),
			ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, jsonSerializerClassName,
			ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, jsonSerializerClassName
		);
		return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties)) {
			{
				setMessageConverter(new MessagingMessageConverter());
			}
		};
	}

}
