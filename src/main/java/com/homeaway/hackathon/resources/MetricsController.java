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

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homeaway.hackathon.client.GraphQLClient;
import com.homeaway.hackathon.model.PropertyMetrics;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MetricsController {

    @Autowired
    private GraphQLClient client;

    /**
     * Return PageViews for the given property
     * @param propertyId property id
     * @return property metrics
     */
    @RequestMapping(value = "/metrics", method = RequestMethod.GET)
    public Optional<PropertyMetrics> metrics(@RequestParam(value = "propertyId", defaultValue = "123.456.7890") String propertyId) {
        log.info("GET Metrics for {}", propertyId);
        String externalId = String.format("%s/%s", GraphQLClient.PREFIX, propertyId);
                try {
                    return client.getMetrics(URLEncoder.encode(externalId, "UTF-8"));
                } catch (Exception ex) {
                    log.error("Error retrieving metrics for : {}", externalId, ex);
                }
        return Optional.empty();
    }

    /**
     * Retrieve metrics for a portfolio of properties in my system
     * @return property metric stats
     */
    @RequestMapping(value = "/metricsStats", method = RequestMethod.GET)
    public List<Optional<PropertyMetrics>> metricsStats() {

        List<String> myProperties = Arrays.asList(
            "EXTID_1/UNITID_1",
            "EXTID_2/UNITID_2");
        return myProperties.stream()
            .map(propertyId -> {
                try {
                    return metrics(propertyId);
                } catch (Exception ex) {
                    log.error("Error retrieving metrics for : {}", propertyId, ex);
                }
                return Optional.<PropertyMetrics>empty();
            }).collect(Collectors.toList());
    }

}
