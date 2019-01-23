package com.homeaway.hackathon.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompetitiveUnit {

	private Double similarityScore;

	private String listingTypeDesc;

	private String propertyTypeDesc;

	private String heading;

	private String listingLink;

	private String unitPhotoUrl;

	private String unitThumbnailUrl;

	private Double distanceFromUnit;

	private Geocode geocode;

}
