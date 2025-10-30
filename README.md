# CSE_2102_Lab6

In this lab, we are using a Spring Java system to compute:

1. The quality of passwords
2. A basic email address validity

We were given starter code for this lab assignment

Important commands to run this lab is to change to the directory demo and then follow these commands:

To run the main (make sure you are in demo directory):
```
./mvnw spring-boot:run
```

Then in a seperate terminal you want to check each of the main endpoints

Just to confirm if the server was set up properly run this command which is to test a simple endpoint:
```
curl -i -X POST http://localhost:8080/say-hi-back
```

Email checker:
```
curl -i -X POST http://localhost:8080/email-address-valid \
  -H 'Content-Type: application/json' \
  -d '{"email":"foo@bar.com"}'
```

Password checker:
```
curl -i -X POST http://localhost:8080/password-quality \
  -H 'Content-Type: application/json' \
  -d '{"password":"MyP@ssw0rd!"}'
```

Then to run the UnitTest:
```
./mvnw test
```