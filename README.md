# Dorm Coordinator

## Prerequisites

- Maven (tested with version 3.6.3)
- Java 8 (or later)

## How to run

Start the tomcat server by running maven command:
```
mvn spring-boot:run
```

Maven will download all the required dependencies and the server will start at port 8080.

If you encounter exception
```
Pnpm install has exited with non zero status. Some dependencies are not installed. Check pnpm command output
```
close the server and run above maven command again.


You can now access the web application by going to http://localhost:8080/ in your browser.