# Kimosabe

webapp for fun with friends  

## Installation

install docker, docker-compose, Java 11, and npm 6.14.6+  
git clone the repository  

## Running the app

### Set up databases

docker-compose up -d  

### Backend

cd api  
./mvnw clean install  
./mvnw spring-boot:run -P dev  

### Frontend

cd ui  
npm install  
npm start  
