package com.homeaway.hackathon.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RentPotential {

	private String type;

	private Integer low_prcentile;

	private Integer mean_percentile;

	private Integer high_percentile;

	private String inflation_factor;

	private Double low;

	private Double mean;

	private Double high;
}
