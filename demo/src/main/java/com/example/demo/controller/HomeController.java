package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class HomeController {

  // commands that can be used to test the endpoints:
  // curl -i -X POST 'http://localhost:8080/say-hi-back?data=Hello%20there'   *this send data*
  // curl -i -X POST 'http://localhost:8080/say-hi-back'         *this uses default value*

  // curl -i -X POST http://localhost:8080/say-hi-back -d 'data=Hello from form'
  // curl -i -X POST http://localhost:8080/say-hi-back

  // @PostMapping(path = "/say-hi-back", consumes = "application/json")
  @PostMapping(path = "/say-hi-back") // remove consumes
  @ResponseStatus(HttpStatus.OK)
  public String say_hi_back(@RequestParam(value = "data", defaultValue = "Enjoy your day!\n") String data) {
    return "Echo: " + data + "\n";
  }

  // curl -i -X POST http://localhost:8080/email-address-valid \
  //   -H 'Content-Type: application/json' \
  //   -d '{"email":"foo@bar.com"}'
  @PostMapping(path = "/email-address-valid", consumes = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public String isEmailValid(@RequestBody EmailRequest body) {
    String email = body != null ? body.getEmail() : null;
    String data = "";
    if (email != null && email.contains("@") && email.contains(".")){
      data = "Valid email address.";
    } else {
      data = "Invalid email address.";
    }
    return "Email check: " + email + " is " + data + "\n";
  }

  // curl -i -X POST http://localhost:8080/password-quality \
  //   -H 'Content-Type: application/json' \
  //   -d '{"password":"MyP@ssw0rd!"}'
  @PostMapping(path = "/password-quality", consumes = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public String isPasswordStrong(@RequestBody PasswordRequest body) {
    String password = body != null ? body.getPassword() : null;
    String data = "";

    if (password != null &&
        password.length() >= 8 &&
        password.matches(".*[A-Z].*") &&
        password.matches(".*[a-z].*") &&
        password.matches(".*\\d.*") &&
        password.matches(".*[!@#$%^&*()].*")) {
      data = "Strong password.";
    } else {
      data = "Weak password.";
    }
    return "Password check: " + data + "\n";
  }

}

class EmailRequest {
  private String email;

  public EmailRequest() { }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}

class PasswordRequest {
  private String password;

  public PasswordRequest() { }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}