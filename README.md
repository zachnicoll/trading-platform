# CAB302 - Electronic Trading Platform
## Group of Three #10
- Zachary Nicoll, n10214453
- Nicholas Ilii, n10223436
- Rory Denis, n10468391

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
