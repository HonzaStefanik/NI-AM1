= HW1 - Integration Issues - Transformation of formats

== Task
Suppose that a customer of a travel agency submits his booking for a tour as a plain text (e.g. submissions via mail). In order to properly process such input in any system, a machine readable and standardized format is needed.

== Data

Implement a simple transformation in Java (simple microservice) that reads the first data format (“Data format 1”) and exports/prints the second format ("Data format 2).
 All inputs and outputs can be represented as String (or files).
 
=== Data format 1:

```
Dear Sir or Madam,

please find the details about my booking below:

===
Tour id: "1"
Location: "Bohemian Switzerland"
Person: "Jan Novak"
===

Regards,
Jan Novak
```

=== Data format 2

```
{
  "id": "1",
  "location": "Bohemian Switzerland",
  "person": {
    "name": "Jan",
    "surname": "Novak"
  }
}
```

= Solution

The application can be built by running `mvn clean package`, which also verifies tests passing.

It can be run with `java -jar *path_to_jar_file*` and it will start on `http://localhost:8080`

There is one endpoint - `POST /transform`. It will accept data format 1 and output data format 2 from the assignment. 
If any of the values is missing, HTTP 400 will be returned instead with an error message.

Example: 

```
$ curl -v -H "Content-type: text/plain" -X POST --data-binary @- http://localhost:8080/transform <<EOF
> Dear Sir or Madam,
> 
> please find the details about my booking below:
> 
> ===
> Tour id: "1"
> Location: "Bohemian Switzerland"
> Person: "Jan Novak"
> ===
> 
> Regards,
> Jan Novak
> bash: warning: here-document at line 237 delimited by end-of-file (wanted `EOF')
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /transform HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.58.0
> Accept: */*
> Content-type: text/plain
> Content-Length: 163
> 
* upload completely sent off: 163 out of 163 bytes
< HTTP/1.1 200 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 86
< Date: Sun, 04 Oct 2020 14:19:45 GMT
< 
* Connection #0 to host localhost left intact
{"id":"1","location":"Bohemian Switzerland","person":{"name":"Jan","surname":"Novak"}}
```

```
$ curl -v -H "Content-type: text/plain" -X POST --data-binary @- http://localhost:8080/transform <<EOF
Dear Sir or Madam,

please find the details about my booking below:

===
Tour id: "1"
Location: "Bohemian Switzerland"
===

Regards,
Jan Novak


> bash: warning: here-document at line 267 delimited by end-of-file (wanted `EOF')
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /transform HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.58.0
> Accept: */*
> Content-type: text/plain
> Content-Length: 146
> 
* upload completely sent off: 146 out of 146 bytes
< HTTP/1.1 400 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 30
< Date: Sun, 04 Oct 2020 14:20:57 GMT
< Connection: close
< 
* Closing connection 0
Received malformed input data.
```

