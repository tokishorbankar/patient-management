# USAGES

* To start the database container and patient-service, run the following command in your terminal:
```shell
docker-compose -f docker/docker-compose.yml up -d
```

* To stop the database container and patient-service, run the following command in your terminal:
```shell
docker-compose -f docker/docker-compose.yml down
```

* Build the Docker image with the following command:
```shell
docker build -t patient-service:latest . --no-cache
```

* Run the Docker container with the following command:
```shell
docker run --name patient-service -d -p 8080:8080 patient-service:latest
```
