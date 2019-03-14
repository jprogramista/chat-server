# Private chats between users an open rooms for all 

`mvn clean package && java -Dserver.port=9090 -jar target/chat-server-0.1.0.jar`

Connect to the web client at: http:://localhost:9090/.

History endpoint http:://localhost:9090/history?recipient=...

Inspired by: [https://github.com/jaysridhar/spring-websocket-server].
