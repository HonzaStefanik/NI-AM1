# HW7 - RESTfull - Conditional GET

## Task
Design and implement a simple service for retrieval of a list of tours.

Example:
```
GET /tours
[
    {
        "id": 1,
        "name": "aaa",
        "customers": []
    },
    {
        "id": 2,
        "name": "bbb",
        "customers": []
    }
]
```
The service should provide support for "Conditinal GET" requests. For this, you need to provide support for HTTP caching using the Last-Modified and ETag headers. Implement two versions using ETags: using strong ETags and using weak ETags. For weak ETag use only the id and the name of each tour. Do not forget to show examples of communication including all HTTP headers (cURL commands and log from communicaiton).

## Solution

Just like in HW06, only parts necessary to complete the assignment are included. 
That means only Tour is implemented as an entity class (customers are simply list of integers inside Tour class).
HATEOAS principle is also not included.

The application will create 3 Tours on startup

```
tours.add(new Tour(0, "first tour"));
tours.add(new Tour(1, "second tour", List.of(1, 2, 3)));
tours.add(new Tour(2, "third tour", List.of(3, 4, 5)));
```

There are few extra endpoints to allow CUD operations which change the date of the last modification returned in the Last-Modified header.

```
POST /tour
body 
{
    "description": "name of tour",
    "customers": [0, 1, 2]
}
    * returns 201 CREATED
    * ID is set automatically - highest current ID is found and 1 is incremented to it; 0 if none found
```

```
DELETE /tour/{id}
    * returns 200 OK, 404 NOT FOUND
```

```
PUT /tour/{id}
{
    "description": "updated name of tour",
    "customers": [0, 1, 2, 3]
}

    * returns 200 OK, 404 NOT FOUND
    * updating ID is not implemented for the sake of simplicity (not the focus of the task)
```

### Conditional GET

The main objective of the task was to create an endpoint which allows conditional GET requests with the Last-Modified header and both strong and weak ETags.

The endpoint is

```
GET /tours

request parameters:
    * strongEtag - can be one of [true, false] (optional, default=true)

request headers: 
    * If-Modified-Since (optional)
    * ETag (optional)

response headers: 
    * Last-Modified
    * ETag

return status codes:
    * 200 OK if resource doesnt fit criteria for cache (+ the tour in response body)
    * 304 NOT_MODIFIED if resource fits criteria for cache (empty respones body)
    * cache criteria is describer lower
```

This endpoint has one optional request param which can be used to switch between the strong and weak ETag which will be in the response.
Default value is true (strong ETag will be in response). Note: this does not affect which ETag (weak / strong) will be read from request headers, the app decides itself based on ETag format.

Weak ETag is a simple string concatenation of all tour IDs and descriptions.
That means the request will be considered as not modified to the ETag criteria if only the customers of the tours have been changed.

Strong ETag is implemented simply by calling the `hashCode()` method on the `List<Tour>`.
This is approach which will be resistant to collisions at the low number of tours we will be using and thus it should uniquely represent the state of our resource.

In addition, calling this endpoint will also log current values of both ETags and Last-Modified date for easier verification.

#### 200 vs 304

If both headers are missing, 200 with the resource is returned. 

If only one is present, then they will have to meet required criteria in order to return 304. 
Request ETag has to match the actual ETag (either weak or strong, application picks based on whether the request ETag contains W/ prefix).
If-Modified-Since has to be a bigger date or equal date to the Last-Modified date.

Dates are represented (converted) internally as `ZonedDateTime`. Date comparison is done using methods `isEqual` and `isAfter` from `ChronoZonedDateTime`.

This date uses a [standard date format described for this header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Last-Modified).

```
Last-Modified: <day-name>, <day> <month> <year> <hour>:<minute>:<second> GMT
``` 

If both headers are present, a single condition satisfied is enough to be considered as a cached resource and 304 will be returned (OR behaviour).
I originally wanted both to be satisfied, but Spring internally changes 200 to 304 when Last-Modified / If-Modified-since is present and I couldn't figure out how to fix it in a proper way.
The easiest solution would be to use a custom header with the prefix `X-`, but I wanted to use standard HTTP headers.

## Example of communication

#### GET without any headers

```
$ curl -v http://localhost:8080/tour

*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /tour HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.58.0
> Accept: */*
> 
< HTTP/1.1 200 
< Last-Modified: Sat, 28 Nov 2020 12:18:22 GMT
< ETag: "330079126"
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sat, 28 Nov 2020 12:18:28 GMT
<
* Connection #0 to host localhost left intact
[{"id":0,"description":"first tour","customers":[]},{"id":1,"description":"second tour","customers":[1,2,3]},{"id":2,"description":"third tour","customers":[3,4,5]}]h

application log: 
2020-11-28 13:18:23.926  INFO 9720 --- [nio-8080-exec-1] hw07.TourController                      : Current strong ETag: 330079126
2020-11-28 13:18:23.926  INFO 9720 --- [nio-8080-exec-1] hw07.TourController                      : Current weak ETag: W/"0first tour1second tour2third tour"
2020-11-28 13:18:23.927  INFO 9720 --- [nio-8080-exec-1] hw07.TourController                      : Current Last-Modified value: Sat, 28 Nov 2020 12:18:22 GMT
```

As expected, 200 with the resource in body was returned.

#### GET with matching strong ETag

```
curl -v -H "ETag:330079126" http://localhost:8080/tour

*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /tour HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.58.0
> Accept: */*
> ETag:330079126
> 
< HTTP/1.1 304 
< Date: Sat, 28 Nov 2020 12:19:29 GMT
< 
* Connection #0 to host localhost left intact

```

304 was returned since ETag matches

#### GET with a non-matching strong ETag

```
 curl -v -H "ETag:42" http://localhost:8080/tour

*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /tour HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.58.0
> Accept: */*
> ETag:42
> 
< HTTP/1.1 200 
< Last-Modified: Sat, 28 Nov 2020 12:18:22 GMT
< ETag: "330079126"
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sat, 28 Nov 2020 12:20:13 GMT
< 
* Connection #0 to host localhost left intact
[{"id":0,"description":"first tour","customers":[]},{"id":1,"description":"second tour","customers":[1,2,3]},{"id":2,"description":"third tour","customers":[3,4,5]}]
```

Resource with 200 OK was returned. Weak ETag works exactly the same, it only has a different format.

Example of weak ETag header format for curl request - `-H 'ETag: W/"0first tour1second tour2third tour"'` 

#### GET with If-Modified-Since being after Last-Modified value

As per application log part from the first request, last modified value is `Sat, 28 Nov 2020 12:18:22 GMT` at the time of writing this README.


```
curl -v -H "If-Modified-Since: Sat, 28 Nov 2020 14:18:22 GMT"  http://localhost:8080/tour

*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /tour HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.58.0
> Accept: */*
> If-Modified-Since: Sat, 28 Nov 2020 14:18:22 GMT
> 
< HTTP/1.1 304 
< Date: Sat, 28 Nov 2020 12:23:28 GMT
< 
* Connection #0 to host localhost left intact
```

Since we sent a time which is after the last modified value, 304 was returned.

#### GET with If-Modified-Since being before Last-Modified value

```
curl -v -H "If-Modified-Since: Sat, 28 Nov 2020 10:18:22 GMT"  http://localhost:8080/tour

*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /tour HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.58.0
> Accept: */*
> If-Modified-Since: Sat, 28 Nov 2020 10:18:22 GMT
> 
< HTTP/1.1 200 
< Last-Modified: Sat, 28 Nov 2020 12:18:22 GMT
< ETag: "330079126"
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sat, 28 Nov 2020 12:24:21 GMT
< 
* Connection #0 to host localhost left intact
[{"id":0,"description":"first tour","customers":[]},{"id":1,"description":"second tour","customers":[1,2,3]},{"id":2,"description":"third tour","customers":[3,4,5]}]
```

Resource with 200 OK was returned as expected.