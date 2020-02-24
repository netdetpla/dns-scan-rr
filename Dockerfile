FROM openjdk:11.0.5-jre-stretch

ADD ["build/libs/dns-scan-rr-1-all.jar", "settings.properties", "/"]

CMD java -jar dns-scan-rr-1-all.jar