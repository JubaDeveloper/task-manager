# Task Manager

The project task manager was created with the aim of improving understanding of how Thymeleaf works and integrating it into a real-world project with Spring.

## Installing

> Ensure that you have Maven installed in your environment.

### Package the application:

```bash
    mvn clean package
```

### After packaging the application, you can run it using the following command:

```bash
  java -jar target/task-management-1.0-SNAPSHOT-jar-with-dependencies.jar
```

> Please note that the application runs on port 8080 by default.

If you need to specify a different port, you can do so by adding the <b>--server.port</b> option followed by the desired port number when running the java -jar command. For example:

```bash
  java -jar target/task-management-1.0-SNAPSHOT-jar-with-dependencies.jar --server.port=9090
```

This would run the application on port 9090 instead.


## Testing the environment

To test and see if everything is working fine and properly set
you can just run:

```bash
  mvn clean test
```