package com.github.simplesteph.udemy.kafka.streams;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
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
import static com.github.simplesteph.udemy.kafka.streams.AppConfigs.*;

public class FeedBackCountWithBranching {

	public static void main(String args[]) {
		
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID_CONFIG);
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET_CONFIG);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, CACHE_MAX_BYTES_BUFFERING_CONFIG);
     
        StreamsBuilder builder = new StreamsBuilder();
		   
        KStream<String, String> usersAndColours = builder.stream(FEEDBACK_COUNT_INPUT,Consumed.with( Serdes.String() , Serdes.String()));

       /*  // branching for negative and positive values.
        *  KStream<String,String> kstream[] = usersAndColours
        		  .filter((key, value) -> value.contains(","))
        		  .selectKey((key, value) -> value.split(",")[0].toLowerCase())
        		  .mapValues((key, value) -> value.split(",")[1].toLowerCase())
        		  .branch(
        				(key,value) -> value.contains(" happy ") || value.contains(" satisfied ") || value.contains("good"),
        				(key,value) -> value.contains("unhappy") || value.contains("unsatisfied") || value.contains("bad"),
        				(key,value) -> true
        				  );
        	      //.transformValues(() -> new FeedbackNegativeTransformer());
        	       * *
        	       */
        KStream<String,String> kstream = usersAndColours
      		  .filter((key, value) -> value.contains(COMMA_SPLIT))
      		  .selectKey((key, value) -> value.split(COMMA_SPLIT)[0].toLowerCase())
      		  .mapValues((key, value) -> value.split(COMMA_SPLIT)[1].toLowerCase());
       
        //transform all the values and get the positive count
        KStream<String,Long> positiveStream = kstream
      	      .transformValues(() -> new FeedbackPositiveTransformer());
        
        //transform all the values and get the negative count
        KStream<String,Long> negativeStream = kstream
        	      .transformValues(() -> new FeedbackNegativeTransformer());
        		
        KeyValueBytesStoreSupplier storePositiveCountSupplier = Stores.persistentKeyValueStore(RATING_COUNT_POSITIVE_STATESTORE);
        
        KeyValueBytesStoreSupplier storeNegativeCountSupplier = Stores.persistentKeyValueStore(RATING_COUNT_NEGATIVE_STATESTORE);
        
        //convert postive kstream into ktable
        KTable<String, Long> positiveRatingCount = positiveStream
                   .groupByKey(Serialized.with(Serdes.String(), Serdes.Long()))
                   .aggregate(
                    () -> new Long(0),
                    ( key, value, count) -> (Long) (value+count)
                    ,Materialized.
                    <String, Long>as(storePositiveCountSupplier)
                    .withKeySerde(Serdes.String())
                    .withValueSerde(Serdes.Long())
                   );
        
       //convert negative kstream into ktable
        KTable<String, Long> negativeRatingCount = negativeStream
                .groupByKey(Serialized.with(Serdes.String(), Serdes.Long()))
                .aggregate(
                 () -> new Long(0),
                 ( key, value, count) -> (Long) (value+count)
                 ,Materialized.
                 <String, Long>as(storeNegativeCountSupplier)
                 .withKeySerde(Serdes.String())
                 .withValueSerde(Serdes.Long())
                );
        
        //write positive rating count on topic
        positiveRatingCount.toStream().to(FEEDBACK_POSITIVE_RATING_COUNT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
        
        //write negative rating count on topic
        negativeRatingCount.toStream().to(FEEDBACK_NEGATIVE_RATING_COUNT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        
        streams.cleanUp();
        streams.start();

        // print the topology
        streams.localThreadsMetadata().forEach(data -> System.out.println(data));

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        
	}
}
