# partner-hackathon-springboot-workshop
Sample application to provide a service executing Marketplace Design GraphQL queries.


## Prerequisites
 * Java 8+
 * Maven 3.2+

## What's Included?
This project was created from a simple springboot REST server. It provides a REST service that executes a GraphQL query to process and return the results.

 * SpringBoot
 * OkHttp client
 * Swagger

## Install

  1. Clone this repo
  2.  `mvn clean install`

## Getting Started

### App configuration
The endpoint of the marketplace GraphQL API and security token needs to be configured in the GraphQL client. Set the following to `src/main/java/com/homeaway/hackathon/client/GraphQLClient.java`
  * ENDPOINT : The URL to the GraphQL endpoint
  * AUTH_TOKEN : An [Authenticated User Session ID](#obtaining-a-session-id)

### Obtaining a session ID
  1. Using Chrome, log into HomeAway as an partner/owner using your normal credentials.
  2. While successfully logged into HomeAway.com, open the Chrome developer tools with the HomeAway web page as your active tab in Chome.
  3. In the dev tools window that opens, navigate to the `Application` tab
  4. Under `Storage`, expand `Cookies` and find the cookies for `HomeAway.com`.  Locate a cookie named `HASESSIONV3`.  The value of this cookie will be your session ID.

### Running the app
  1. `./start.sh`
  2. Navigate to `http://localhost:8080/swagger-ui.html`

### Use cases

- Retrieving marketplace design and page views for a given property
- Retrieving markteplace design and page views for a set of properties
