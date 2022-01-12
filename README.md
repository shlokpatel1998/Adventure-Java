# Adventure-Java

This project was created in my Object-Oriented Programming class as a custom rebuild of the popular game **Colossal Cave Adventure** created by Will Crowther in 1977!

To run program (refer to pom.xml)

- clean:  mvn clean
- checkstyle: mvn checkstyle:checkstyle  (results show up in target/site)
- compile : mvn compile
- junit: mvn test
- execute: mvn exec:java
- jar: mvn assembly:assembly
 (this will make two jar files.  Use the one with dependencies)
> to run your executable jar: java -jar <pathtoexecutablejarwithdependencies> <flags>
