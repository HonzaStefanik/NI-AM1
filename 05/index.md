# HW5 - RESTful API Design - resources, URIs, operations

## Task

A company runs an application for a travel agency. Design a RESTful service where the resources have the base URI: http://t-a.org

The data model contains following entities: country (1) ← (0..N) location (1) ← (0..N) tour (0..N) → (0..N) customer.

Your solution should cover all CRUD operations for each entity/resource.

## Instructions

Design view on data model and design and describe the URIs of resources (At least one resource should provide filtering using query string parameter)
```
Example:
/customers - list of customers
/... - ...
```
Describe all available operations, associated method and possible status codes
```
/customers - get list of customers
HTTP method: GET
Status codes: 200 - ok, 400 - bad request
```
Do not forget to include HATEOAS principles

## Solution

Note: I misunderstood the assignment and implemented the service as a whole instead of just documenting the API.
The documentation still exists, however, the app has to be run and it can be found as described below.

There are also some things which were affected by auto-generating the documentation.
For example, all endpoints have listed 404 as a possible return status code due to global exception handler.
This is obviously not true for all endpoints - eg GET /tour will never return a 404. 

The API is documented via Swagger UI
 
 * http://localhost:8080/swagger-ui/index.html (swagger)

 * http://localhost:8080/v3/api-docs (api documentation)
 
In general, the status codes will be 200 if operation was successful and 404 if a requested entity was not found (eg GET /country/nonExistentId). 
The only exception are PUT mappings which will create the entity if it is not found.

>At least one resource should provide filtering using query string parameter

This is implemented for the Tour controller. Tours can be filtered by the country they take place in, such as

```GET /tour?country=de```

>country (1) ← (0..N) location (1) ← (0..N) tour (0..N) → (0..N) customer

I decided to implement following behaviour when deleting entities.

* When a **country** is deleted, all its **locations** are also deleted
* When a **location** is deleted, tours in that location will have their location set to null (ie a tour can exist without a location)
* When a **tour** is deleted, their **customers** will have that tour removed from them (ie customer can exist without a tour)
* When a **customer** is deleted, their **tours** will have that customer removed (ie tour can exist without customers)



 
 
