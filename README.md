# REST API Project Tracker

A REST-based Project tracker (Jira/To-Do) 

## Links

**[Task overview](TASK.md)**

**[Live version of Swagger UI](https://task-manager-swagger.herokuapp.com/swagger-ui/index.html)**

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
cd docker
docker-compose up -d
```

The local version will be available at: http://localhost:8080/swagger-ui/index.html
