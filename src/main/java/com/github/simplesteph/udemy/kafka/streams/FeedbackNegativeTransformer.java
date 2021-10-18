package com.github.simplesteph.udemy.kafka.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;

public class FeedbackNegativeTransformer implements ValueTransformer<String, Long> {

    @Override
    public void init(ProcessorContext processorContext) {
    	
    }

    @Override
    public Long transform(String str) {
		// TODO Auto-generated method stub
		
		List<String>  list =  Arrays.asList(str.split(" "));
		Long negativeCount = list.stream().filter(value -> (value.equals("bad")|| value.equals("unhappy") || value.equals("unsatisfied"))).count();
		System.out.println("negativeCount :"+negativeCount);
		
		return negativeCount;
	}

    @Override
    public void close() {

    }
}


