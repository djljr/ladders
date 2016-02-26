#!/bin/bash

wget -O /tmp/datomic.zip https://my.datomic.com/downloads/free/${DATOMIC_VERSION}
unzip /tmp/datomic.zip

cd datomic-free-${DATOMIC_VERSION}
cp config/samples/free-transactor-template.properties transactor.properties
sed "s/host=localhost/host=0.0.0.0/" -i transactor.properties
sed "s/# data-dir=data/data-dir=\/data/" -i transactor.properties
sed "s/# log-dir=log/log-dir=\/log/" -i transactor.properties
