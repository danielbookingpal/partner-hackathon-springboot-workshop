/**
Copyright 2018 Expedia Group, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.homeaway.hackathon.resources;

import com.homeaway.hackathon.client.GraphQLClient;
import com.homeaway.hackathon.model.LocationRentPotential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class RentPotentialController {

    @Autowired
    private GraphQLClient client;

    /**
     * Return PageViews for the given property
     * @return rent potential
     */
    @RequestMapping(value = "/rentPotential", method = RequestMethod.GET)
    public Optional<LocationRentPotential> rentPotential(@RequestParam(value = "latitude") Double latitude,
            @RequestParam(value = "longitude") Double longitude,
            @RequestParam(value = "bathroom") Float bathroom, @RequestParam(value = "bedroom") Integer bedroom,
            @RequestParam(value = "sleep") Integer sleep) {
        log.info("GET Rent Potential for lat {}, lng {}", latitude, longitude);
        try {
            return client.getRentPotentialPrediction(latitude, longitude, bathroom, bedroom, sleep);
        } catch (Exception ex) {
            log.error("Error retrieving Rent Potential for lat {}, lng {}", latitude, longitude, ex);
        }
        return Optional.empty();
    }

}
