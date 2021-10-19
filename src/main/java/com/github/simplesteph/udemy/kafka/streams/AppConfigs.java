package com.github.simplesteph.udemy.kafka.streams;

public class AppConfigs {

	public final static String REWARDS_STORE_NAME = "CustomerRewardsStore";
    public final static String REWARDS_TEMP_TOPIC = "CustomerRewardsTempTopic";
    public final static String APPLICATION_ID_CONFIG = "CustomerRewardsStore";
    public final static String BOOTSTRAP_SERVERS_CONFIG = "0.0.0.0:9092";
    public final static String AUTO_OFFSET_RESET_CONFIG = "earliest";
    public final static String CACHE_MAX_BYTES_BUFFERING_CONFIG = "0";
    public final static String FEEDBACK_COUNT_INPUT = "feedback-count-input";
    public final static String FEEDBACK_COUNT_OUTPUT = "feedback-count-output";
    public final static String RATING_COUNT_POSITIVE_STATESTORE = "rating-count-positive-statestore";
    public final static String RATING_COUNT_NEGATIVE_STATESTORE = "rating-count-negative-statestore";
    public final static String FEEDBACK_POSITIVE_RATING_COUNT_TOPIC = "feedback-positive-rating-count";
    public final static String FEEDBACK_NEGATIVE_RATING_COUNT_TOPIC = "feedback-negative-rating-count";
    public final static String COMMA_SPLIT =",";
    
}
