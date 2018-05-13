FROM openjdk:10

ADD target/xml-analyzer-*.jar /app.jar

EXPOSE 8080

VOLUME /tmp

CMD java -jar /app.jar
