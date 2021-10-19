package com.github.simplesteph.udemy.kafka.streams;

import static com.github.simplesteph.udemy.kafka.streams.AppConfigs.APPLICATION_ID_CONFIG;
import static com.github.simplesteph.udemy.kafka.streams.AppConfigs.AUTO_OFFSET_RESET_CONFIG;
import static com.github.simplesteph.udemy.kafka.streams.AppConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static com.github.simplesteph.udemy.kafka.streams.AppConfigs.CACHE_MAX_BYTES_BUFFERING_CONFIG;
import static com.github.simplesteph.udemy.kafka.streams.AppConfigs.RATING_COUNT_POSITIVE_STATESTORE;
import static com.github.simplesteph.udemy.kafka.streams.AppConfigs.FEEDBACK_COUNT_OUTPUT;
import static com.github.simplesteph.udemy.kafka.streams.AppConfigs.FEEDBACK_COUNT_INPUT;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.github.simplesteph.udemy.kafka.streams.model.RatingRequest;
import com.github.simplesteph.udemy.kafka.streams.model.RatingResponse;

public class FeedBackCountWithJSONSchema {
	
	public static void main(String args[]) {
		
		Serde<RatingRequest> ratingRequestSerde = new JsonSerde<>(RatingRequest.class);
		Serde<RatingResponse> ratingResponseSerde = new JsonSerde<>(RatingResponse.class);
		
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID_CONFIG);
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET_CONFIG);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, CACHE_MAX_BYTES_BUFFERING_CONFIG);
     
        StreamsBuilder builder = new StreamsBuilder();
		   
        KStream<String, RatingRequest> usersAndColours = builder.stream(FEEDBACK_COUNT_INPUT,Consumed.with( Serdes.String() , ratingRequestSerde));

        KStream<Long,RatingRequest> kstream = usersAndColours
      		  .filter((key, value) -> null != value && null != value.getComment())
      		  .selectKey((key, value) -> value.getLocationId());
       
        //transform all the values and get the positive and negative count
        KStream<Long ,RatingResponse> positiveStream = kstream
      	      .transformValues(() -> new FeedbackTransformer());
        
        KeyValueBytesStoreSupplier storePositiveCountSupplier = Stores.persistentKeyValueStore(RATING_COUNT_POSITIVE_STATESTORE);
        
        //convert postive kstream into ktable
        KTable<Long, RatingResponse> positiveRatingCount = positiveStream
                   .groupByKey(Serialized.with(Serdes.Long(), ratingResponseSerde))
                   .aggregate(
                      RatingResponse :: new,
                    new FeedbackResponseAggregator()
                    ,Materialized.
                    <Long, RatingResponse>as(storePositiveCountSupplier)
                    .withKeySerde(Serdes.Long())
                    .withValueSerde(ratingResponseSerde)
                   );
        
        //write positive rating count on topic
        positiveRatingCount.toStream().to(FEEDBACK_COUNT_OUTPUT, Produced.with(Serdes.Long(), ratingResponseSerde));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        
        streams.cleanUp();
        streams.start();

        // print the topology
        streams.localThreadsMetadata().forEach(data -> System.out.println(data));

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        
	
		
	}
}
