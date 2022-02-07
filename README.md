# REST API Project Tracker

A REST-based Project tracker (Jira/To-Do) 

## Links

**[Task overview](TASK.md)**

**[Live version of Swagger UI](https://task-manager-swagger.herokuapp.com/swagger-ui/index.html)** (contains example data set)

## Well documented and well-structured APIs:
* https://mailchimp.com/developer/marketing/api/root/
* https://www.coingecko.com/api/documentations/v3#/

## Thought process
The task specifies two distinct entities: **Project** and **Task**.
We can assume that a **Task** cannot exist by itself without a **Project**.

Thus, we arrive at the following public REST interface:

### GET
* **/api/projects/** - get all projects (plus sorting & pagination) 
* **/api/projects/{id}** - get project details 
* **/api/projects/{id}/tasks** - get project tasks (plus sorting & pagination) 
* **/api/tasks/{id}** - get task details 

### DELETE
* **/api/projects/{id}** - delete a project and all related tasks  
* **/api/tasks/{id}** - delete a task 

### POST
* **/api/projects/** - add new project (optionally with tasks) 
* **/api/project/{id}/tasks** - add new tasks to a project 

### PUT 
* **/api/projects/{id}** - update project details 
* **/api/tasks/{id}** - update a task 


## How to run

```shell
cd docker;
docker-compose up -d;
```

The local version will be available at: http://localhost:8080/swagger-ui/index.html

**Note**: local version is deployed with mock data **[03-data.sql](docker/db/03-data.sql)** and without data persistence volume not to pollute the host machine

## Technologies used

* Spring boot and underlying Jackson as JSON Serializer, Spring web mvc with TomCat as a web server and Spring validation as input validation libraries 
* Spring Data with Hibernate as an ORM library
* Springdoc for OpenAPI V3 implementation and Swagger UI integration
* Lombok & Spring dev tools for ease of development
* jUnit v5 as a testing framework
* ModelMapper for ease of converting objects from DTO to Entity and vice-versa
* Maven as a build tool
* Mockaroo as a data mocking tool
* PostgreSQL as a DBMS
* Docker & Docker-Compose as a deployement tool

## Patterns and principles used
* **SOLID**, I've strived to split responsibility of every layer 
  * DTOs as intermittent state of data, between user and persistence layers
  * Controller advisory as a way to catch exceptions and pass on to user
* **TDD** when developing logic layer, I have, firstly, designed tests and only afterwards the implementation
* Also, wrote some functional code when iterating through collections


