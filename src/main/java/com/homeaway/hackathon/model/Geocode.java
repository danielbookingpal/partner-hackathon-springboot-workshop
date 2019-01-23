package com.homeaway.hackathon.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Geocode {

	private Double longitude;

	private Double latitude;
}
