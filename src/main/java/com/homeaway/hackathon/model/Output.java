package com.homeaway.hackathon.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class Output {

	private Double lat;

	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) private Double longitude;

	private Float bathroomNum;

	private Integer bedroomNum;

	private Integer sleepNum;

	private Integer num_nn;

	private String model_name;

	private RentPotential rentpotential;
	private RentPotential rentpotential_ha;
	private RentPotential rentpotential_pure;
	private RentPotential rentpotential_pure_ha;

	public Double getLong() {
		return longitude;
	}

	public void setLong(Double longitude) {
		this.longitude = longitude;
	}
}
