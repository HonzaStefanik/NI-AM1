# HW6 - RESTfull - Asynchronous operation

## Task

Design and implement simple service for removal of a tour from a database.

```DELETE /tour/{id}```

Assume that the operation requires confirmation by human. Design and implement the operation in an asynchronous manner. Note: It should not be implemented as synchronous operation! You can simulate the confirmation as a delay of 10s or implement another service to confirm deletion.

## Solution

The solution only implements parts necessary to perform required operations. 
For example, relations to other entities like in hw05 is not implemented). HATEOAS principle is also not included. 

At the start of the app, it will create 10 tours stored in a static list (in order to reduce time needed to create tours).

The app contains following endpoints:

```
GET /tour - list all tours
    * returns 200 OK

DELETE /tour/{id} - delete tour asynchronously
    * returns 200 OK, 404 NOT FOUND, 400 BAD REQUEST
    * 404 is returned when a tour with given ID does not exist
    * 400 is returned when a tour with given ID already is in the process of being deleted

GET /tour/delete-status - lists statuses of all tours marked for deletion
    * returns 200 OK

GET /tour/delete-status/{id} - displays status of a given tour marked for deletion
    * returns 200 OK, 404 NOT FOUND
```

When the DELETE endpoint is called, it will immediately return HTTP 201 and the Location header with content '/tour/delete-status/{id}' (id from request).

The tour wont be deleted until 10 seconds pass from the request creation.

During this time, it will be visible in GET /tour endpoint and it will also be visible in both delete-status endpoints with status IN_PROGRESS.

After 10 seconds pass, it will no longer be available in GET /tour and it will have status DELETED in delete-status endpoints.