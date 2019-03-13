# Private chats between users an open rooms for all 

Terminal 1:

`mvn clean package && java -Dserver.port=9090 -jar target/chat-server-0.1.0.jar`

Terminal 2:

`java -Dserver.port=9099 -jar target/chat-server-0.1.0.jar`

Connect to the web client at: http:://localhost:9090/ and http:://localhost:9099/. Will work without CORS.

Inspired by: [https://github.com/jaysridhar/spring-websocket-server].
