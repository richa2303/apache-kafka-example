package com.github.simplesteph.udemy.kafka.streams.serdes;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import com.github.simplesteph.udemy.kafka.streams.model.RatingRequest;
import com.github.simplesteph.udemy.kafka.streams.model.RatingResponse;

public class RatingSerdes extends Serdes{
	
	 static public final class RatingRequestSerde extends WrapperSerde<RatingRequest> {
	        public RatingRequestSerde() {
	            super(new JsonSerializer<>(), new JsonDeserializer<>());
	        }
	    }

	    static public Serde<RatingRequest> RatingRequest() {
	    	RatingRequestSerde serde =  new RatingRequestSerde();

	        Map<String, Object> serdeConfigs = new HashMap<>();
	        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, RatingRequest.class);
	        serde.configure(serdeConfigs, false);

	        return serde;
	    }
	    
	    static public final class RatingResponseSerde extends WrapperSerde<RatingResponse> {
	        public RatingResponseSerde() {
	            super(new JsonSerializer<>(), new JsonDeserializer<>());
	        }
	    }

	    static public Serde<RatingResponse> RatingResponse() {
	    	RatingResponseSerde serde =  new RatingResponseSerde();

	        Map<String, Object> serdeConfigs = new HashMap<>();
	        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, RatingResponse.class);
	        serde.configure(serdeConfigs, false);

	        return serde;
	    }
}
