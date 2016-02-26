#!/bin/bash


/datomic-free-${DATOMIC_VERSION}/bin/transactor /datomic-free-${DATOMIC_VERSION}/transactor.properties 2>&1 > /dev/null &

java -jar /app/ladders.jar "$@"
