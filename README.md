# Electronic Trading Platform

This project served as a learning experience for developing a full-stack application in Java. The application itself is a mock trading platform for use within an organisation that wishes to trade resources between departments. Each department is assigned a number of "credits" that they can spend on resources being sold by other departments.

- JavaFX has been used to create the GUI
- A Rest API has been implemented from scratch using Java's HttpServer package
- PostgreSQL has been used for information retrieval and storage
- GitHub Actions have been implemented to automate testing and building of the application

### Dependencies
Gradle is the build tool used to handle dependencies for each package. Dependencies should therefore be automatically handled when the packages are built.

### Build
Build all 3 packages by running this in the root directory:
```shell
./gradlew build
```
All tests are automatically run when this command is executed. Note that the build will fail if any tests are failing.

### Packages

- `shared` contains classes that are used in both the `server` and `client` package
- `client` contains all the client-side code, including a JavaFX GUI application and capability to communicate with the REST API
- `server` contains all the backend code responsible for the Rest API, including routing, authentication, and database communication
