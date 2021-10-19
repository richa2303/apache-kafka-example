package com.github.simplesteph.udemy.kafka.streams;

import org.apache.kafka.streams.kstream.Aggregator;

import com.github.simplesteph.udemy.kafka.streams.model.RatingResponse;

public class FeedbackResponseAggregator implements Aggregator<Long, RatingResponse, RatingResponse>{

	@Override
	public RatingResponse apply(Long key, RatingResponse value, RatingResponse aggregate) {
		// TODO Auto-generated method stub
		if(null != aggregate.getNegativeCount()) {
			aggregate.setNegativeCount(aggregate.getNegativeCount()+value.getNegativeCount());
		}else {
			aggregate.setNegativeCount(0L+value.getNegativeCount());
		}
		
		System.out.println("Negative Count :"+aggregate.getNegativeCount());
		
		if(null != aggregate.getPositiveCount()) {
			aggregate.setPositiveCount(aggregate.getPositiveCount()+value.getPositiveCount());
		}else {
			aggregate.setPositiveCount(0L+value.getPositiveCount());
		}
		aggregate.setPositiveCount(aggregate.getPositiveCount()+value.getPositiveCount());
		System.out.println("Positive Count :"+aggregate.getPositiveCount());
		return aggregate;
	}

}
