FROM openjdk:8u181-jdk-alpine3.8
COPY ["pack", "."]

RUN ["java", "-version"]
RUN ["javac", "io/github/rybalkinsd/Main.java"]
RUN ["jar", "cfm", "Main.jar", "MANIFEST.MF", "io/github/rybalkinsd/Main.class"]
CMD ["java", "-jar", "Main.jar"]
