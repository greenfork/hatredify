FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/hatredify.jar /hatredify/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/hatredify/app.jar"]
