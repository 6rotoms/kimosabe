# Kimosabe

webapp for fun with friends  

[![Build Status](https://cloud.drone.io/api/badges/6rotoms/kimosabe/status.svg?ref=refs/heads/master)](https://cloud.drone.io/6rotoms/kimosabe)

## Installation

install docker, docker-compose, Java 11, and node 12.18.4+  
git clone the repository  

## Running the app

### Set up databases/services

docker-compose up -d  

### Backend

cd api  
./mvnw clean install  
./mvnw spring-boot:run -P dev  

### Frontend

cd ui  
npm install  
npm start  
