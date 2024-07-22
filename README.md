## Read Me First
This is a simple ```Spring Boot Application``` that runs with an embedded ```Tomcat Server``` and an in-memory ```H2 Database```. 
In this application H2 Database is also configured to save the data on file system.
When the source is built it automatically produces an executable jar file in the target folder. When the jar is run,
the Tomcat server and the H2 database will be spin up and start running on port 3000.

# Business Use Case
A customer has asked you for a way to provide on-demand monitoring of various
unix-based servers without having to log into each individual machine and opening up
the log files found in ```/var/log```. The customer has asked for the ability to issue a REST
request to a machine in order to retrieve logs from ```/var/log``` on the machine receiving the
REST request.

# Getting Started
* This project runs on jdk(Java) 13
* The system will need a Java 13 installation for the service to run
* This is a maven project. Maven needs to be installed on the system
* The source can be built using ``` mvn clean install ``` command from the base folder. The base folder in the example below is ```Source```
* After the source is built, a similar message will be displayed :
```dtd
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.993 s
[INFO] Finished at: 2024-07-21T23:16:58-07:00
[INFO] ------------------------------------------------------------------------

```
# Running the service
* Simple way to run the service is to type ```java -jar target/log-search-application-1.0-SNAPSHOT.jar```
* Once the service is successfully started, a similar message will be shown on the terminal :
```dtd
2024-07-21 23:30:06.064  INFO 11921 --- [           main] DeferredRepositoryInitializationListener : Spring Data repositories initialized!
2024-07-21 23:30:06.126  INFO 11921 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 3000 (http) with context path ''
2024-07-21 23:30:06.130  INFO 11921 --- [           main] com.cribl.LogSearchApplication           : Started LogSearchApplication in 4.265 seconds (JVM running for 4.854)

```
* Next, the service can be used in the following way:
    * Open another terminal to do the following operations.
    * Note there is a ```/v1``` in the path to enable versioning.
* You will have to make two rest calls to setup the log data and indexing.
* You will need a sample file from which logs will be generated for service use. This sample file can
contain text, even paragraphs of normal text etc. One such sample has been provided in the ```LogsSample```
folder with the name ```sample-cribl-for-log.txt```.
1. Issue the first rest command to create a sample log file out of this. Note the sample log file generated will be
```service.log``` in the ```/var/log``` folder. These folders/folder-structure will be auto-generated when the 1st REST
API call is made:
```dtd
curl -F 'file=@"<your-directory-path>/LogsSample/sample-cribl-for-log.txt"' localhost:3000/v1/logs/create 
```
The ```service.log``` file has a strict logging format. This format is strctly followed throughout the course of this exercise :
```timestamp [log-level] [request-id] : log message ```
for example :
```
2024-07-21T15:48:58.940538 [INFO] [f4e11b28-a9f2-4f6d-88c4-4f9320df2464] : Cribl is the Data Engine for IT and Security. Our mission is to empower enterprises to unlock the value of all their data.
```
2. Second REST API call is to index the ```service.log``` generated in 1/. This step ensures that the log message is tokenized, 
and indices are created.
```dtd
curl -F 'file=@"<your-directory-path>/Source/var/logs/service.log"' localhost:3000/v1/logs/index 
```
3. Finally, you can now search on logs. Note that the tokenization algorithm is basic and it might miss some keywords 
from properly indexing them. This is a limitation. In order to test the service you should try bunch of different 
keywords. Also note, that like search on keywords in supported, however, the searches are case-sensitive 
for the keyword. Basic search on log-file-name and keyword combinations are supported at this time. log-file-name is 
mandatory. Pagination has been implemented with a max mage size of 50 (enforced).
```dtd
curl -XGET http://localhost:3000/v1/logs/?logFileName=service.log&keyword=Crible 
```
You will get a response like the following:
```dtd
{
  "logFileName": "service.log",
  "logLines": [
    "7/21/24, 3:48 PM [INFO] [d7d2f70a-a83d-460a-a7a1-42c4a6d5d7e6] : We built our data processing engine specifically for IT and Security and made sure our Cribl product suite is vendor-agnostic.",
    "7/21/24, 3:48 PM [ERROR] [a57f591e-54ad-4a1d-bccd-e0cc4214a888] : Fortune 1000 companies around the world depend on Cribl���������s innovative and proven product suite.",
    "7/21/24, 3:48 PM [DEBUG] [171dd4c1-536f-42ed-b871-01682a4761fa] : And Cribl Search is the industry���������s first search-in-place solution.",
    "7/21/24, 3:48 PM [DEBUG] [a7c6a638-78f6-4cd1-bcea-b5b816cb0b09] : Founded in 2018, Cribl is a remote-first workforce with headquarters in San Francisco, California.",
    "7/21/24, 3:48 PM [INFO] [db6edfac-3da0-4747-a931-01140a29df70] : With Cribl, IT and Security teams have choice, control, and flexibility to adapt to data needs that are always changing.",
    "7/21/24, 3:48 PM [ERROR] [349bd232-9e77-48cf-bf0e-9f1e23957a19] : Cribl Stream is the industry���������s leading observability pipeline. Cribl Edge is an intelligent, vendor-neutral agent."
  ],
  "pageSize": 50,
  "nextOffset": 2
}
```
The format you receive is not pretty, you may make it pretty by installing jquery using brew. If you install jquery, 
you can run the following:
```dtd
curl -XGET http://localhost:3000/v1/logs/?logFileName=service.log&keyword=Crib | jq .
```
The response will be pretty like above.

# Assumptions & Considerations
* For the given problem, the log file is generated by reading all the lines from the input file. The real world scenario
is different : the log will be appended to the log file (at the end) and then can be indexed for faster keyword retrieval
* User management module is a separate problem to solve altogether, the assumption is that the users
that are searching logs are:
    * User account that they/admin can manage
    * Are authenticated users,
    * Have proper authorization/permission to call the APIs
    * Another service like AWS IAM can be used to manage user authentication and authorization
* Rate limiting is done using another separate service so that each user has a quota of throughput they can use 
every second.

# Reference Documentation
For further reference on how Spring works, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.0.M1/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [H2 Database](https://www.h2database.com/html/main.html)

# Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
  