PRE-NOTES
- IntelliJ was used to create the project
- Project was built and run using Java 21 and Springboot v3.3.6
- Docker is recommended to run Apache Kafka and Zookeeper
- Tested using Postman
- May need Lombok and Docker plugin for Intellij

STEPS TO RUN
- Open project using IDE
- **OPTIONAL** Go to root folder /umpisa then (in cmd) run "docker-compose up -d" (do not include quotes)
- Run Springboot project
- Open postman and use this base URL plus the endpoint mappings http://localhost:8080/api/reservation/

ADDITIONAL INFO
- You may still run the app without Docker or Apache Kafka
- Kafka MQ is used to send notifications asynchronously. In case of failure, a fallback is run on the same thread as the request
- See application.properties for H2 details
- Only single DB table used for this Demo
- Only some field validations are added for this Demo
- Spring Security not included in this Demo
- Response message only shown when there are errors
- Added API to update mode of notification per reservation 
- Test cases does not cover if Kafka messages were properly consumed
- Scheduler runs every hour. Reminder will start sending notification 4 hours prior and every hour until the actual time.
- Scheduler can be configured to run every 3 seconds (comment out some lines in ReservationScheduler and ReservationService)
 
