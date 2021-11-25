mvn clean package;
docker build -t chargefactoryapp:0.0.1 .;
docker-compose up;
mvn clean;