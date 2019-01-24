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
package com.homeaway.hackathon.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.homeaway.hackathon.model.Geocode;
import com.homeaway.hackathon.model.PropertyCompetitiveUnits;
import com.homeaway.hackathon.model.LocationRentPotential;
import com.homeaway.hackathon.model.PropertyInfo;
import org.json.simple.JSONObject;
import org.springframework.boot.json.JsonSimpleJsonParser;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeaway.hackathon.model.PropertyMetrics;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GraphQLClient {

    private static final String SYSTEM_ID = "MYBOOKINGPAL";
    private static final String ADVERTISER_ID = "724471";
    // External System and Supplier ID
    public static String PREFIX = SYSTEM_ID + "/" + ADVERTISER_ID;
    private static final String AUTH_TOKEN = "d221a2e5-c82d-452a-ad02-de0cea33d15d";
    private static final String RENT_POTENTIAL_AUTH_TOKEN = "258c38e7-c0ea-4791-9230-a259c33ed031";
    private static final String ENDPOINT = "https://xapi.homeaway.com/rezfest/graphql";
    private static final String RENT_POTENTIAL_ENDPOINT = "https://integration.homeaway.com/services/external/rentPotentialPrediction";

    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Map<String, PropertyInfo> propertyInfos = new HashMap<>();

    private final PropertyInfo DEFAULT_PROPERTY_INFO = getPropertyInfo(28.26068250853662, -81.64670320000005,
            "Championsgate Resort - 6BD/5BA Pool Home - Sleeps 12 - Gold - RCG6564");

    public GraphQLClient() {
        propertyInfos.put("MYBOOKINGPAL%2F724471%2F1234952674%2F1234952674", DEFAULT_PROPERTY_INFO);
        propertyInfos.put("MYBOOKINGPAL%2F724471%2F1234584931%2F1234584931", getPropertyInfo(28.37791, -81.69009,
                "Greater Groves - Pool Home 4BD/3BA - Sleeps 8 - Gold - RGG408"));
        propertyInfos.put("MYBOOKINGPAL%2F724471%2F1234584934%2F1234584934", getPropertyInfo(28.29384, -81.66179,
                "Hampton Lakes - Pool Home 5BD/3BA - Sleeps 10 - Silver - RHL504"));
        propertyInfos.put("MYBOOKINGPAL%2F724471%2F1234584956%2F1234584956", getPropertyInfo(28.3276209585605, -81.5877721,
                "Oakwater Resort - 2BD/2BA Condo Near Disney - Sleeps 4 - Gold - ROW225"));
        propertyInfos.put("MYBOOKINGPAL%2F724471%2F1234585093%2F1234585093", getPropertyInfo(28.321542860255907, -81.59693959999998,
                "Windsor Hills Condo 3Bedroom/2Bathroom Sleeps 6 Gold - RWH383"));
        propertyInfos.put("MYBOOKINGPAL%2F724471%2F1234771580%2F1234771580", getPropertyInfo(28.3247304, -81.444574,
                "Sonoma - 7BD/9BA Pool Home - Sleeps - RSN7852"));
        propertyInfos.put("MYBOOKINGPAL%2F724471%2F1234778666%2F1234778666", getPropertyInfo(28.320951, -81.602068,
                "Windsor Hills Resort - 5BD/4BA Pool Home - Sleeps 10 - Platinum - RWH575"));
        propertyInfos.put("MYBOOKINGPAL%2F724471%2F1234811463%2F1234811463", getPropertyInfo(28.238232908529, -81.60113515,
                "Solterra Resort Platinum - 032 Pool Home"));
        propertyInfos.put("MYBOOKINGPAL%2F724471%2F1234893930%2F1234893930", getPropertyInfo(28.26117640853682, -81.6472372,
                "Champions Gate Resort - 5BD/5BA Pool Home - Sleeps 10 - Platinum - RCG553"));
        propertyInfos.put("MYBOOKINGPAL%2F724471%2F1234954958%2F1234954958", getPropertyInfo(28.26105930853674, -81.64328260000002,
                "Championsgate - 4BD/3BA Town House - Sleeps 8 - Platinum - RCG4921"));
        propertyInfos.put("MYBOOKINGPAL%2F724471%2F1234958810%2F1234958810", getPropertyInfo(28.23418560852714, -81.6010963,
                "Solterra Resort - 7BD/5.5BA Pool Home - Sleeps 14 - RST7565"));
    }
    /**
     * POST query to GraphQL endpoint
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    private String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url(url)
            .addHeader("x-homeaway-thin-ui-ha-session", AUTH_TOKEN)
            .post(body)
            .build();
        Response response = CLIENT.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Retrieve metrics for a propertyId
     * @param propertyId
     * @return
     */
    public Optional<PropertyMetrics> getMetrics(String propertyId) {
        String query = getMetricsQuery(propertyId);
        log.info("query {}", query);
        try {
            // Do Post
            String response = doPostRequest(ENDPOINT, query);

            // De-Serialize
            Object obj = new JsonSimpleJsonParser().parseMap(response);
            JSONObject jo = (JSONObject) obj;
            Map data = (Map)jo.get("data");
            JSONObject property = (JSONObject)data.get("property");
            if (property!=null) {
                //Return
                return Optional.ofNullable(MAPPER.readValue(property.toString(), PropertyMetrics.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Construct the Query
     * @param propertyId
     * @return
     */
    private String getMetricsQuery(String propertyId) {
        String rawQuery = "{"
            + "\"operationName\":null,"
            + "\"variables\":{\"propertyId\":\"%s\"},"
            + "\"query\":\"query ($propertyId: String!) "
            + "{  property(propertyId: $propertyId) {"
            + "    legacyId"
            + "    externalId"
            + "    metrics {"
            + "      market {"
            + "        name "
            + "      }"
            + "      scorecard {"
            + "        pageviews {"
            + "          value"
            + "        }"
            + "      }"
            + "    }"
            + "  }"
            + "}"
            + "\"}";
        return String.format(rawQuery, propertyId);
    }

    public Optional<PropertyCompetitiveUnits> getCompetitiveSet(String propertyId) {
        String query = getCompetitiveUnitsQuery(propertyId);
        log.info("query {}", query);
        try {
            // Do Post
            String response = doPostRequest(ENDPOINT, query);

            // De-Serialize
            Object obj = new JsonSimpleJsonParser().parseMap(response);
            JSONObject jo = (JSONObject) obj;
            Map data = (Map)jo.get("data");
            JSONObject property = (JSONObject)data.get("property");
            if (property != null) {
                //Return
                final PropertyCompetitiveUnits propertyCompetitiveUnits = MAPPER.readValue(property.toString(), PropertyCompetitiveUnits.class);
                if (propertyCompetitiveUnits != null) {
                    PropertyInfo propertyInfo = getPropertyInfo(propertyId);
                    propertyCompetitiveUnits.setPropertyInfo(propertyInfo);
                }
                return Optional.ofNullable(propertyCompetitiveUnits);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private PropertyInfo getPropertyInfo(String propertyId) {
        PropertyInfo propertyInfo = propertyInfos.get(propertyId);
        if (propertyInfo == null) {
            propertyInfo = DEFAULT_PROPERTY_INFO;
        }
        return propertyInfo;
    }

    private Geocode getGeoCode(double latitude, double longitude) {
        return Geocode.builder().latitude(latitude).longitude(longitude).build();
    }

    private PropertyInfo getPropertyInfo(double latitude, double longitude, String propertyName) {
        return PropertyInfo.builder().geocode(getGeoCode(latitude, longitude)).propertyName(propertyName).build();
    }

    /**
     * Construct the Query
     * @param propertyId property id
     * @return competitive set for property
     */
    private String getCompetitiveUnitsQuery(String propertyId) {
        String rawQuery = "{"
                + "\"operationName\":null,"
                + "\"variables\":{\"propertyId\":\"%s\"},"
                + "\"query\":\"query ($propertyId: String!) "
                + "{  property(propertyId: $propertyId) {"
                + "    legacyId"
                + "    externalId"
                + "    competitiveUnits {"
                + "      similarityScore"
                + "      listingTypeDesc"
                + "      propertyTypeDesc"
                + "      heading"
                + "      distanceFromUnit"
                + "      listingLink"
                + "      unitPhotoUrl"
                + "      unitThumbnailUrl"
                + "      geocode {"
                + "        longitude"
                + "        latitude"
                + "      }"
                + "    }"
                + "  }"
                + "}"
                + "\"}";
        return String.format(rawQuery, propertyId);
    }

    public Optional<LocationRentPotential> getRentPotentialPrediction(Double latitude, Double longitude,
            Float bathroom, Integer bedroom, Integer sleep) {
        String query = getRentPotentialQuery(latitude, longitude, bathroom, bedroom, sleep);
        log.info("query {}", query);
        try {
            // Do Post
            String response = doPostRequest(RENT_POTENTIAL_ENDPOINT, query);

            // De-Serialize
//            Object obj = new JsonSimpleJsonParser().parseMap(response);
//            JSONObject jo = (JSONObject) obj;
//            Map data = (Map)jo.get("data");
//            JSONObject property = (JSONObject)data.get("property");
//            if (property!=null) {
                //Return
                return Optional.ofNullable(MAPPER.readValue(response, LocationRentPotential.class));
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private String getRentPotentialQuery(Double latitude, Double longitude, Float bathroom, Integer bedroom, Integer sleep) {
        String rawQuery = "{\n" +
                "    \"systemId\": \"" + SYSTEM_ID + "\",\n" +
                "    \"advertiserId\": \"" + ADVERTISER_ID + "\",\n" +
                "    \"authToken\": \"" + RENT_POTENTIAL_AUTH_TOKEN + "\",\n" +
                "    \"input\": {\n" +
                "        \"lat\": %s,\n" +
                "        \"long\": %s,\n" +
                "        \"bathroomNum\": %s,\n" +
                "        \"bedroomNum\": %s,\n" +
                "        \"sleepNum\": %s,\n" +
                "        \"num_nn\": %s\n" +
                "    }\n" +
                "}";
        return String.format(rawQuery, latitude, longitude, bathroom, bedroom, sleep, 10);
    }
}
