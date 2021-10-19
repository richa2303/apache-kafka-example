
package com.github.simplesteph.udemy.kafka.streams.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "locationId",
    "positiveCount",
    "negativeCount",
    "ratingId",
    "remark"
})
public class RatingResponse {

    @JsonProperty("locationId")
    private Long locationId;
    @JsonProperty("positiveCount")
    private Long positiveCount;
    @JsonProperty("negativeCount")
    private Long negativeCount;
    @JsonProperty("ratingId")
    private Long ratingId;
    
    public RatingResponse() {
    	
    }
    
    public RatingResponse(Long locationId, Long positiveCount, Long negativeCount, Long ratingId, String remark) {
		//super();
		this.locationId = locationId;
		this.positiveCount = positiveCount;
		this.negativeCount = negativeCount;
		this.ratingId = ratingId;
		this.remark = remark;
	}

	@JsonProperty("remark")
    private String remark;

    @JsonProperty("locationId")
    public Long getLocationId() {
        return locationId;
    }

    @JsonProperty("locationId")
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public RatingResponse withLocationId(Long locationId) {
        this.locationId = locationId;
        return this;
    }

    @JsonProperty("positiveCount")
    public Long getPositiveCount() {
        return positiveCount;
    }

    @JsonProperty("positiveCount")
    public void setPositiveCount(Long positiveCount) {
        this.positiveCount = positiveCount;
    }

    public RatingResponse withPositiveCount(Long positiveCount) {
        this.positiveCount = positiveCount;
        return this;
    }

    @JsonProperty("negativeCount")
    public Long getNegativeCount() {
        return negativeCount;
    }

    @JsonProperty("negativeCount")
    public void setNegativeCount(Long negativeCount) {
        this.negativeCount = negativeCount;
    }

    public RatingResponse withNegativeCount(Long negativeCount) {
        this.negativeCount = negativeCount;
        return this;
    }

    @JsonProperty("ratingId")
    public Long getRatingId() {
        return ratingId;
    }

    @JsonProperty("ratingId")
    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public RatingResponse withRatingId(Long ratingId) {
        this.ratingId = ratingId;
        return this;
    }

    @JsonProperty("remark")
    public String getRemark() {
        return remark;
    }

    @JsonProperty("remark")
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public RatingResponse withRemark(String remark) {
        this.remark = remark;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("locationId", locationId).append("positiveCount", positiveCount).append("negativeCount", negativeCount).append("ratingId", ratingId).append("remark", remark).toString();
    }

}
