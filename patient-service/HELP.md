# USAGES

## Start the database contain
> First Run Docker Desktop or Docker Engine Refer to the [Docker documentation](https://docs.docker.com/get-started/)
> Refer to the Docker Compose file in the docker folder.
 
* To start the database container, run the following command in your terminal:
```shell
docker-compose up -d &
```

* To stop the database container, run the following command in your terminal:
```shell
docker-compose down
```

* Build the Docker image with the following command:
```shell
docker build -t patient-service:latest .
```

* Run the Docker container with the following command:
```shell
docker run --name patient-service -d -p 8080:8080 patient-service:latest
```
