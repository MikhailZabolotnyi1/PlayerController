# Testing  API [PlayerController](http://3.68.165.45/swagger-ui.html#/player-controller/createPlayerUsingGET)

# Stack:
- Java 17;
- testNG (running in 3 thdreads);
- RestAssured;
- Allure (Installation guide);

# Terminal commands:
- mvn clean test - removes files created during build in the project directory, then build.
- mvn allure:report - report generator (placed PlayerController/target/site/index.html)
- allure serve allure-results - automatically open report in browser


# Assignment
- [x] Write a framework for app testing. 
- [x] Write few positive and few negative autotests for each controller.
- [x] Find possible bugs. Critical ones cover with autotests.
- [x] Make test run in 3 treads (Middle and Senior lvl). 
- [x] Tests should be written as if you're working with a production system, where every change is costly and risky.
- [x] Upload the project to a public Git repository and provide the link.
Additional:
- [x] *Add Allure 
- [x] **Add logging
- [x] ***Add  framework configuration (app url, thread count, etc.)

