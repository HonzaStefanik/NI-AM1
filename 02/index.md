# HW2

## Tasks

### Telnet

#### Assignment

* create HTTP GET request using telnet to url http://147.32.233.18:8888/NI-AM1-ApplicationProtocols/httpTelnet1 with following specification:
    - user agent - fit-telnet
    - only text/html in utf8 and en-US should be accepted
    
* create HTTP POST request using telnet to url http://147.32.233.18:8888/NI-AM1-ApplicationProtocols/httpTelnet2 with following specification:
    - set referer to “mi-mdw”
    - content type application/x-www-form-urlencoded
    - send data in format data=fit    
    
#### Solution 


```$ telnet 147.32.233.18 8888
Trying 147.32.233.18...
Connected to 147.32.233.18.
Escape character is '^]'.
GET /NI-AM1-ApplicationProtocols/httpTelnet1  HTTP/1.1
Host: 147.32.233.18 
User-Agent: fit-telnet
Accept: text/html;charset=UTF-8
Accept-Charset: UTF-8
Accept-Language: en-US

HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: text/html;charset=UTF-8
Content-Length: 3
Date: Sat, 03 Oct 2020 12:28:00 GMT

O

Connection closed by foreign host.
```

```
$ telnet 147.32.233.18 8888
Trying 147.32.233.18...
Connected to 147.32.233.18.
Escape character is '^]'.
POST /NI-AM1-ApplicationProtocols/httpTelnet2 HTTP/1.1
Host: 147.32.233.18
Referer: mi-mdw
Content-Type: application/x-www-form-urlencoded
Content-Length: 8

data=fit
HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: text/html;charset=UTF-8
Content-Length: 3
Date: Sat, 03 Oct 2020 13:48:17 GMT

OK

```

### cURL

#### Assignment

* Open this page and follow instructions http://147.32.233.18:8888/NI-AM1-ApplicationProtocols/protocol

#### Solution

```
$ curl http://147.32.233.18:8888/NI-AM1-ApplicationProtocols/protocol/welcome
OK
Your next page is /protocol/get
send GET parameter "name" with value "gotten" 
set Header "Accept" to "text/plain"

$ curl -H "Accept: text/plain" -X GET http://147.32.233.18:8888/NI-AM1-ApplicationProtocols/protocol/get?name=worship
OK
Your next page is /protocol/post 
send POST parameter "name" with value "offensive"
and set Header "Accept" is "text/plain" 
and set Header "Accept-Language" is "en-US" 

 curl -d "name=offensive"  -H "Accept: text/plain" -H "Accept-Language: en-US" -X POST http://147.32.233.18:8888/NI-AM1-ApplicationProtocols/protocol/post
OK
Your next page is /protocol/referer 
change referer to value "cease"
set Header "Accept" is "text/html" 

$ curl -H "Accept: text/html" --refer "cease" http://147.32.233.18:8888/NI-AM1-ApplicationProtocols/protocol/referer
OK
Your next page is /protocol/useragent
and change User-Agent to value "sanctions"
and set Header "Accept-Language" is "en-US" 

$ curl -H "Accept-Language: en-US" -H "User-Agent: sanctions" http://147.32.233.18:8888/NI-AM1-ApplicationProtocols/protocol/useragent
OK
Your next page is /protocol/cookie 
send cookie called "name" with value "test"

$ curl --cookie "name=test"  http://147.32.233.18:8888/NI-AM1-ApplicationProtocols/protocol/cookie
OK
Your next page is /protocol/auth 
authenticate by "through:described"
set Header "Accept" is "text/html" 

$ curl -H "Accept: text/html" --user "through:described"  http://147.32.233.18:8888/NI-AM1-ApplicationProtocols/protocol/auth
OK
Your final result is: purposeful 
```

