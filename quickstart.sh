./keygen.sh
cd authentication-jwt
mvn clean compile package
cd ../user-management
mvn clean compile package
cd ..
docker compose up
