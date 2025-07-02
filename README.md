# patient-management

## Build Patent Service

```shell
mvn clean install
```

* To start the database container and patient-service, run the following command in your terminal:

```shell
docker-compose -f patient-service/docker/docker-compose.yml up -d
```

* To stop the database container and patient-service, run the following command in your terminal:

```shell
docker-compose -f patient-service/docker/docker-compose.yml down
```
