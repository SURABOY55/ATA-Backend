# ATA-Backend

*** How to start ***

1. mvn clean

2. mvn install

3. mvn spring-boot:run

after project started,Project will import data from .json file to embedded database.

*** curl to test api ***

curl --location 'http://localhost:8080/job_data?gender=Male&job_title=Software%20Developer&sort_type=DESC&sort=salary&fields=job_title%2Cgender%2Csalary&salary=120000'
