#!/bin/bash
# Starts the web server assuming "mvn clean install" has completed successfully.

if ! [ -f target/rezfest-springboot-service-*.jar ]; then
    echo "You must 'mvn clean install' before starting the server." 1>&2
    exit 1
fi

java -jar target/rezfest-springboot-service-*.jar