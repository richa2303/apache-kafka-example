package com.github.simplesteph.udemy.kafka.streams;

import java.util.Arrays;
import java.util.List;

import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import com.github.simplesteph.udemy.kafka.streams.model.RatingRequest;
import com.github.simplesteph.udemy.kafka.streams.model.RatingResponse;

public class FeedbackTransformer implements ValueTransformer<RatingRequest, RatingResponse> {

    @Override
    public void init(ProcessorContext processorContext) {
    	
    }

    @Override
    public RatingResponse transform(RatingRequest ratingRequest) {
		// TODO Auto-generated method stub
		
    	List<String>  list =  Arrays.asList(ratingRequest.getComment().split(" "));
		Long positiveCount = list.stream().filter(value -> (value.equals("happy")|| value.equals("good") || value.equals("satisfied"))).count();
		System.out.println("Positive :"+positiveCount);
		Long negativeCount = list.stream().filter(value -> (value.equals("bad")|| value.equals("unhappy") || value.equals("unsatisfied"))).count();
		System.out.println("negativeCount :"+negativeCount);
		
		
		RatingResponse ratingResponse = new RatingResponse();
		ratingResponse.setNegativeCount(negativeCount);
		ratingResponse.setPositiveCount(positiveCount);
		ratingResponse.setLocationId(ratingRequest.getLocationId());
		ratingResponse.setRatingId(ratingRequest.getRatingId());
		ratingResponse.setRemark(ratingRequest.getComment());
		
		return ratingResponse;
	}

    @Override
    public void close() {

    }
}




