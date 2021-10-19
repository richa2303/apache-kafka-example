
package com.github.simplesteph.udemy.kafka.streams.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "locationId",
    "ratingId",
    "comment"
})
public class RatingRequest {

    @JsonProperty("locationId")
    private Long locationId;
    @JsonProperty("ratingId")
    private Long ratingId;
    @JsonProperty("comment")
    private String comment;

    public RatingRequest() {
	} 
    
    public RatingRequest(Long locationId, Long ratingId, String comment) {
		//super();
		this.locationId = locationId;
		this.ratingId = ratingId;
		this.comment = comment;
	}

	@JsonProperty("locationId")
    public Long getLocationId() {
        return locationId;
    }

    @JsonProperty("locationId")
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public RatingRequest withLocationId(Long locationId) {
        this.locationId = locationId;
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

    public RatingRequest withRatingId(Long ratingId) {
        this.ratingId = ratingId;
        return this;
    }

    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    public RatingRequest withComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("locationId", locationId).append("ratingId", ratingId).append("comment", comment).toString();
    }

}
