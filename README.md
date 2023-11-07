# WebSocketProxy_JavaxAPI

The current project serves as websocket proxy.
It uses Javax API's and runs on Jetty Server.

![image](https://github.com/Spandana806/WebSocketProxy_JavaxAPI/assets/11814206/7ee056b1-83e4-46a6-9342-69a321257a12)

Edit the target websocket [URL](https://github.com/Spandana806/WebSocketProxy_JavaxAPI/blob/master/src/main/java/ClientToProxy.java#L10) here in ClientToProxy.java
To run, use the maven command - jetty:run
Jetty server will start on port 8080.
A websocket endpoint will be registered - ws://localhost:8080/ws/subscribe/
It will redirect to the specified Target URL and a websocket will be opened.

