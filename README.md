# xml-analyzer
[![Build Status](https://travis-ci.org/jpodeszwik/xml-analyzer.svg?branch=master)](https://travis-ci.org/jpodeszwik/xml-analyzer)

## configure
This project uses code generation. To open it in ide you should configure annotation processing:
https://immutables.github.io/apt.html

It might be also necessary to configure generated sources root.

## build
```mvn package```

## run
```java -jar target/xml-analyzer-*.jar```

Docker image:
```docker run -p 8080:8080 -d jpodeszwik/xml-analyzer```
