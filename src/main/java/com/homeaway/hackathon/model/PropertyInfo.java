package com.homeaway.hackathon.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyInfo {

	private String propertyName;
	
	private Geocode geocode;

}
