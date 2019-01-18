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
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.boot.json.JsonSimpleJsonParser;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeaway.hackathon.model.Metrics;
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

    private static final String AUTH_TOKEN = "<TOKEN TO BE INSERTED HERE>";
    private static final String ENDPOINT = "<GRAPHQL ENDPOINT TO BE INSERTED HERE>";

    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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
}
