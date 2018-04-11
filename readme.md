# Metro Project

This project uses basic Java framework build using Gradle. Consists of multiple modules as below.

## Overview
This project contains 2 modules:
1. Test-Api is built with using simple junit framework. 
2. SystemsProgram contains standalone java classes to run any programs

### Test-Api
1. This module consists of test class MetroTest for calling the Metro apis and finding the time for next bus.
2. Contains other supporting classes required for the test
3. Used Model approach for deserializing json with Object Mapper.

To run all the tests from the project:
./gradlew clean build test 

To run from specific module:
./gradlew :module-a:test


### SystemsProgram
1. This module contains the java class which give disk usage information
2. Added some tests to test the main class