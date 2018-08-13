# ryanair
Ryanair interview task

How to run:
1. mvn clean package
2. deploy and run produced war "interconnecting-flights-service-0.0.2-SNAPSHOT.war" in application container (Tomcat or any other)
3. run test query : http://localhost:9898/ifs/interconnections?departure=DUB&arrival=WRO&departureDateTime=2018-08-20T07:00&arrivalDateTime=2018-08-23T21:00
