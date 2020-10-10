# Kimosabe Backend API

API that communicates with front-end Kimosabe application.  


### Features
- User Authentication via Sessions
- Searching with cached IGDB data


### Running Locally

This API requires both Redis and PostgreSQL to be running. Run `docker-compose up -d` to orchestrate containers.  
Then run './mvnw spring-boot:run -P dev' to start a local development version of the API. The local development  
version comes with 2 pre-existing users. If you need more users, they can be added to the import.sql file.  

| UUID                                 	| username 	| password  	|
|--------------------------------------	|----------	|-----------	|
| 41096c69-47db-4fdb-9a84-bef10e571546 	| user1    	| password1 	|
| b72cfbb3-565d-4711-81ad-e6cdf1f349c1 	| user2    	| password1 	|


### Documentation

[Link to Swagger API Documentation](https://app.swaggerhub.com/apis-docs/6rotoms/kimosabe-api/0.0.1)