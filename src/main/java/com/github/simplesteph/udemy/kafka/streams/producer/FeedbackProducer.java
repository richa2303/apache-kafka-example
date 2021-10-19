package com.github.simplesteph.udemy.kafka.streams.producer;

import static com.github.simplesteph.udemy.kafka.streams.AppConfigs.BOOTSTRAP_SERVERS_CONFIG;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.simplesteph.udemy.kafka.streams.model.RatingRequest;

public class FeedbackProducer {

	public static void main(String args[])throws Exception
		{
			Properties props = new Properties();
			props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
			props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
			  "org.apache.kafka.common.serialization.StringSerializer");
			props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.connect.json.JsonSerializer");
			props.put("schema.registry.url", "http://127.0.0.1:8081");
			Producer<String, JsonNode> producer = new KafkaProducer<String, JsonNode>(props);
		
			ObjectMapper objectMapper = new ObjectMapper();
			String topic = "feedback-count-input";
			RatingRequest ratingRequest = new RatingRequest(2L ,4L, "Good food good service bad parking space...");
			JsonNode  jsonNode = objectMapper.valueToTree(ratingRequest);
			ProducerRecord<String, JsonNode> record = new ProducerRecord<String,
					JsonNode>(topic, jsonNode); 
			producer.send(record);
			producer.flush();
			producer.close();
			
		}
}
