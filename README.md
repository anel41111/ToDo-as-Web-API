# REST API Project Tracker

A simple Web-based Project tracker. 

## Links

**[Task overview](TASK.md)**

**[Live version of Swagger UI](TODO:add)**

## Thought process
The task specifies two distinct entities: **Project** which consists of **Tasks**, 
as such we can assume that a **Task** cannot exist by itself without a **Project**.

Thus, we arrive at the following API public interface:

### GET
* "/api/projects/" - get all projects (plus sorting & pagination)
* "/api/projects/{id}" - get project details
* "/api/projects/{id}/tasks" - get project tasks (plus sorting & pagination)
* "/api/tasks/{id}" - get project details

### DELETE
* "/api/projects/{id}" - delete a project and all related tasks 
* "/api/tasks/{id}" - delete a task 

### POST
* "/api/projects/" - add new project (optionally with tasks)
* "/api/project/{id}/tasks" - add new tasks to a project

### PUT 
* "/api/projects/{id}" - update project details
* "/api/tasks/{id}" - update a task 


## How to run
TODO

## Postman collection
