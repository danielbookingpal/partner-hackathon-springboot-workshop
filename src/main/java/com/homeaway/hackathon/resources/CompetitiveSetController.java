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
import com.homeaway.hackathon.model.PropertyCompetitiveUnits;
import com.homeaway.hackathon.model.PropertyMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class CompetitiveSetController {

    @Autowired
    private GraphQLClient client;

    /**
     * Return PageViews for the given property
     * @param propertyId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/competitiveSet", method = RequestMethod.GET)
    public Optional<PropertyCompetitiveUnits> competitiveSet(@RequestParam(value = "propertyId") String propertyId) {
        log.info("GET Metrics for {}", propertyId);
        String externalId = String.format("%s/%s", GraphQLClient.PREFIX, propertyId);
                try {
                    return client.getCompetitiveSet(URLEncoder.encode(externalId, "UTF-8"));
                } catch (Exception ex) {
                    log.error("Error retrieving metrics for : {}", externalId, ex);
                }
        return Optional.empty();
    }

}
