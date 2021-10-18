package com.github.simplesteph.udemy.kafka.streams;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;

public class FeedbackCountWithStateStore {
	
	public static void main(String args[]) {
		
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "favourite-colour-java");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

     
       // KafkaStreams streams = new KafkaStreams(builder.build(), config);
        StreamsBuilder builder = new StreamsBuilder();
		   
        KStream<String, String> usersAndColours = builder.stream("feedback-count-input",Consumed.with( Serdes.String() , Serdes.String()));

        //StoreBuilder kvStoreBuilder = Stores.keyValueStoreBuilder(storeSupplier,Serdes.String(), Serdes.Long());
        		
        //builder.addStateStore(kvStoreBuilder);

        System.out.println("Hello before Kstream Processing");
        
        KStream<String,Long> kstream = usersAndColours
        		  .filter((key, value) -> value.contains(","))
        		  .selectKey((key, value) -> value.split(",")[0].toLowerCase())
        	      .transformValues(() -> new FeedbackNegativeTransformer());
        		
        System.out.println("Hello before KTable Processing");
        
        KeyValueBytesStoreSupplier storeSupplier = Stores.persistentKeyValueStore("rating-count-statestore");
        
        Materialized<String, Long, KeyValueStore<Bytes, byte[]>> ratingStateStoreMaterialized =
                Materialized.
                    <String, Long>as(storeSupplier)
                    .withKeySerde(Serdes.String())
                    .withValueSerde(Serdes.Long());
        
        KTable<String, Long> bankBalance = kstream
                   .groupByKey(Serialized.with(Serdes.String(), Serdes.Long()))
                   .aggregate(
                    () -> new Long(0),
                    ( key, value, count) -> (Long) (value+count)
                    ,ratingStateStoreMaterialized
                   );
        
        System.out.println("Hello after KTable Processing");
        
        //
        bankBalance.toStream().to("feedback-rating-count", Produced.with(Serdes.String(), Serdes.Long()));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        
        streams.cleanUp();
        streams.start();

        // print the topology
        streams.localThreadsMetadata().forEach(data -> System.out.println(data));

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    
	}

}
