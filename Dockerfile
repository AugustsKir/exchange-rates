FROM openjdk:18
COPY jar/exchange-rate-app.jar /exchange-rate-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","exchange-rate-app.jar"]
CMD ["load_data"]
