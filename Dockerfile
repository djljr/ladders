FROM java:8-jre

# Getting Datomic Free
ENV DATOMIC_VERSION 0.9.5350
COPY setup-datomic.sh /
RUN chmod +x /setup-datomic.sh && /setup-datomic.sh

RUN mkdir /data
VOLUME /data

RUN mkdir /log
VOLUME /log

# Add application
RUN mkdir /app
COPY target/ladders.jar /app

# startup
WORKDIR /
COPY start.sh /
RUN chmod +x /start.sh

EXPOSE 3000 4334 4335 4336

ENTRYPOINT ["/start.sh"]