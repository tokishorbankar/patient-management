# Patient Management

## Patent Service

* To start the database container and patient-service, run the following command in your terminal:

```shell
cd patient-service/docker
docker-compose up -d &
```

* To stop the database container and patient-service, run the following command in your terminal:

```shell
cd patient-service/docker
docker-compose down
```

* Build Patient Service JAR file:

```shell
cd patient-service/
mvn clean install package
```

* Build the Docker image with the following command:

```shell
cd patient-service/
docker build -t patient-service:latest . --no-cache
```

* Run the Docker container with the following command:

```shell
cd patient-service/
docker run --name patient-service -d -p 8080:8080 patient-service:latest
```

## Patient-service microservice

```shell
cd patient-service
mvn clean package
