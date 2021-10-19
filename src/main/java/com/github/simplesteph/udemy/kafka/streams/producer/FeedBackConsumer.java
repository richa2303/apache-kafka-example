package com.github.simplesteph.udemy.kafka.streams.producer;

import static com.github.simplesteph.udemy.kafka.streams.AppConfigs.BOOTSTRAP_SERVERS_CONFIG;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.github.simplesteph.udemy.kafka.streams.model.RatingRequest;

public class FeedBackConsumer {

	public static void main(String args[]) {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		//props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.JsonDeserializer");
		props.put("schema.registry.url", "http://localhost:8081");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RatingRequest.class.getName());

		String topic = "feedback-input-count-json";
		final Consumer<String, RatingRequest> consumer = new KafkaConsumer<String, RatingRequest>(props);
		consumer.subscribe(Arrays.asList(topic));

		try {
		  while (true) {
		    ConsumerRecords<String, RatingRequest> records = consumer.poll(100);
		    for (ConsumerRecord<String, RatingRequest> record : records) {
		      System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());
		    }
		  }
		} finally {
		  consumer.close();
		}
	}
}
