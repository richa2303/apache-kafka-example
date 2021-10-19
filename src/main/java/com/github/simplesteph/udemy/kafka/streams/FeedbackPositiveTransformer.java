package com.github.simplesteph.udemy.kafka.streams;

import java.util.Arrays;
import java.util.List;

import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;

public class FeedbackPositiveTransformer implements ValueTransformer<String, Long> {

    @Override
    public void init(ProcessorContext processorContext) {
    	
    }

    @Override
    public Long transform(String str) {
		// TODO Auto-generated method stub
		
    	List<String>  list =  Arrays.asList(str.split(" "));
		Long positiveCount = list.stream().filter(value -> (value.equals("happy")|| value.equals("good") || value.equals("satisfied"))).count();
		System.out.println("Positive :"+positiveCount);
		
		return positiveCount;
	}

    @Override
    public void close() {

    }
}



