# WODSS
WODSS stands for "Workshop Distributed Systems", a module of the FHNW (University of applied science).

# TODOOO
In the WODSS module each team has assignment to build a software similar to <a href="https://trello.com/" target="_blank">Trello</a>. Our version is called TODOO.

## API Documentation
###`POST /token`
To  login into our application an to do any further action a token has to be send with every request. In order to request a new token you must send a JSON containing email address and password to the `/token` resource via `POST`.

JSON example:
```json
{
    "email": "hans.muster@fhnw.ch",
    "password": "myverysecretpassword"
}
```
On server side the password will be hashed and compared with the saved hash on the database. If the passwords do match, a new token in form of a unique identifier will be created, cached on the server and returned to the client.

Token example: `550e8400-e29b-11d4-a716-446655440000`

This token you must add to the request header for every authorized action.

Request header example: `x-session-token: 550e8400-e29b-11d4-a716-446655440000`

A generated token has a time to live. After expiration the token will be deleted automatically and the user will be logged out. The time to live is set by default to one hour.

###`DELETE /token`

A `DELETE` request on `/token` will delete the token given in the request header (e.g. `x-session-token: 550e8400-e29b-11d4-a716-446655440000`) from the server's cache. This means, this token will be invalid for any further action. With an invalid token the user will be redirected to the login page. To delete a token, it is obvious that a user must have a valid token before, so must be logged in.

###`GET /users`
To send a `GET` request on the resource `/users` the user must be logged in an have a valid token. The token must be included in the headers of the `GET` request. This request will return a list of users that are subscribed to a specific board, so to send such a request the board must be part of the request body, e.g.
```
{
    "board": {
       "id":1
    }
}
```

###`GET /user/{id}`
To send a `GET` request on the resource `/user/{id}` the user must be logged in an have a valid token. This resource will return the user profile of the logged in data. So on server side there will be a verification whether the token and the url id are corresponding to each other.

###`POST /user`
This resource is needed to register a new user. Therefore no valid token is needed. In the request body you must send the name, the email address and the password, e.g.:
```
{
    "name": "Hans Muster",
    "email": "hans.muster@fhnw.ch",
    "password": "mysecretpassword"
}
```
On server side it will be checked whether the user has already registered. If so, then a `CONFLICT` code will be returned. Otherwise the user will be notified by email to verify his email address. The user cannot log in until the email address has been verified.

###`DELETE /user/{id}`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource deletes the user's profile from the application. A user can only delete his or her own profile not another one. After deletion the token will be invalid.
A user profile can only be deleted if the user has no boards owned or subscribed, otherwise a `CONFLICT` code will be returned.

###`PUT /user/{id}`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource updates a user profile. This is only possible by the same user logged in.

###`PUT /user/{id}/logindata`
This resource is used for two different actions, for validation a user's email address and to reset a user's password. A valid token must not be send within the request headers in order to do one of these actions. 
#### Validate user's email address
Send the validation code within the request body to this resource via `PUT`, e.g.
```
{
    "validationCode": "550e8400-e29b-11d4-a716-446655440000"
}
```
As soon as a validation code has been detected, the resource will validate the email address of the user that has sent this request.
#### Request a reset code
To request a request code send a JSON object with the following content to this resource:
```
{
    "doReset": "true"
}
```
#### Reset user's password
To reset a user's password send a JSON object with the following form to this resource:
```
{
    "resetCode": "550e8400-e29b-11d4-a716-446655440000",
    "password": "mynewverysecretpassword"
}
```

###`GET /boards`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will return all boards where the user which is requesting is the owner or subscriber.


###`GET /board/{id}`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will return a specific board where the user which is requesting is the owner or subscriber.


###`POST /board`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will create a new board. All users assigned to this board will be notified by email.

###`DELETE /board/{id}`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will delete a board. This is only possible if the requesting user is the owner of the board. All tasks under this board will be deleted automatically including all attachements.

###`PUT /board/{id}`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will delete a board. This is only possible if the requesting user is the owner of the board.

###`GET /tasks`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will return either all tasks of all boards the user is owner or subscriber. Or if the request body contains a specific board, then only the tasks of this board will be returned, e.g.
```
{
    "board": {
       "id":1
    }
}
```
###`GET /task/{id}`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will return a specific task. This is only possible if the requesting user is owner or subscriber of the board the task is part of.

###`POST /task`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will create a new task. This is only possible if the requesting user is the owner or subscriber to the board where the task should be created. The request must be a multipart form request, because a user can attach files to the task. Here an example how the request body should look like:
```
{
    "attachments":[],
    "dueDate":null,
    "description":"Description",
    "id":null,
    "state":"TODO",
    "assignee":null,
    "creationDate":1462795595126,
    "doneDate":null,
    "board":{
        "owner":{
            "name":"Blab",
            "id":2,
            "email":"Blubub@fhnw.ch"
        },
        "id":1,
        "title":"Baord",
        "users":[
            {
                "name":"Blab",
                "id":2,
                "email":"Blubub@fhnw.ch"
            }
        ]
    }
}
```
Here an example how the HTTP headers look like:
```
x-session-token: 422af739-3ebc-4031-9c26-c1a2874ea4b0
Content-Length: 667851
Content-Type: multipart/form-data; boundary=jXManvK-aWi2nIpXaj6fObz-A4QUbY
Host: localhost:8080
Connection: Keep-Alive
User-Agent: Apache-HttpClient/4.5.1 (Java/1.8.0_92)
Accept-Encoding: gzip,deflate

--jXManvK-aWi2nIpXaj6fObz-A4QUbY
Content-Disposition: form-data; name="info"
Content-Type: application/json; charset=UTF-8
Content-Transfer-Encoding: 8bit

{BODY}
```

###`DELETE /task/{id}`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will delete a specific task including all its attachments. This is only possible if the requesting user is owner or subscriber to the board this task is part of.

###`PUT /task/{id}`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will modify a specific task including all its attachments. This is only possible if the requesting user is owner or subscriber to the board this task is part of.

###`GET /attachment/{id}`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will return a specific attachment. This is only possible if the requesting user is owner or subscriber of the board that contains the task for which this attachment belongs.

###`DELETE /attachment/{id}`
To send this request the user must be logged in and the valid token must be included in the request headers. This resource will delete a specific attachment. This is only possible if the requesting user is owner or subscriber of the board that contains the task for which this attachment belongs.
