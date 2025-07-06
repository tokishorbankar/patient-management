# USAGES

* To start the database container and patient-service, run the following command in your terminal:
```shell
cd docker
docker-compose up -d &
```

* To stop the database container and patient-service, run the following command in your terminal:
```shell
cd docker
docker-compose down
```

* Build Patient Service JAR file:
```shell
mvn clean install
```

* Build the Docker image with the following command:
```shell
docker build -t patient-service:latest . --no-cache
```

* Run the Docker container with the following command:
```shell
docker run --name patient-service -d -p 8080:8080 patient-service:latest
```

* To run the tests, use the following command:
```shell
mvn clean test
```

* To run the application, use the following command:
```shell
mvn spring-boot:run
```

* To open the Swagger UI, navigate to:
```curl
http://localhost:8080/swagger-ui/index.html
```
