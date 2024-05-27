FROM eclipse-temurin:17.0.10_7-jre-alpine
RUN mkdir /opt/app
COPY build/libs/*.jar /opt/app/
RUN rm /opt/app/*plain.jar
RUN mv /opt/app/*.jar /opt/app/wishlist.jar
EXPOSE 8080

CMD ["java", "-jar", "/opt/app/wishlist.jar"]


