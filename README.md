# wordsmith

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Setup with Quarkus and HTMX
- https://docs.quarkiverse.io/quarkus-web-bundler/dev/
- https://htmx.org/docs/

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/wordsmith-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Host the application on GKE (macOS)

### Prerequisites

- Google Cloud SDK
- Docker

### Steps

authenticate to GCP

    gcloud components update
    gcloud auth login
    gcloud auth configure-docker gcr.io

create a new google could project

    gcloud projects create wordsmith-1234
    gcloud config set project wordsmith-1234

set required secrets in google cloud

    gcloud services enable secretmanager.googleapis.com
    printf "secret-mongo-password" | gcloud secrets create mongoConnectionString --data-file=-

add secret access to the service account

    gcloud projects add-iam-policy-binding wordsmith-1234 \
    --role="roles/secretmanager.secretAccessor" \
    --member=serviceAccount:<my-service-account-email>

package the application

    ./mvnw package

build the docker image
    
    docker build -f src/main/docker/Dockerfile.jvm -t gcr.io/wordsmith-1234/wordsmith-jvm:v1 .

enable the container registry

    gcloud services enable containerregistry.googleapis.com

login to the docker registry

    docker login -u oauth2accesstoken -p "$(gcloud auth print-access-token)" https://gcr.io

push the image

    docker push gcr.io/wordsmith-1234/wordsmith-jvm:v1
    docker tag gcr.io/wordsmith-1234/wordsmith-jvm:v1 gcr.io/wordsmith-1234/wordsmith-jvm:latest
    docker push gcr.io/wordsmith-1234/wordsmith-jvm:latest

enable cloud run api

    gcloud services enable run.googleapis.com

deploy the app using cloud run

    gcloud run deploy wordsmith \
    --image gcr.io/wordsmith-1234/wordsmith-jvm:latest \
    --platform managed \
    --region europe-west1 \
    --allow-unauthenticated