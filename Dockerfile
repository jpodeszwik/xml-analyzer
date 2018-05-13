FROM openjdk:10

COPY target/xml-analyzer-*.jar /app.jar

EXPOSE 8080

VOLUME /tmp

CMD java -jar /app.jar
