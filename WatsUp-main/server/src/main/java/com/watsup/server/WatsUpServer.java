package com.watsup.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication
@RestController
public class WatsUpServer {

	private static final String FROM_EMAIL = "watsup2425@gmail.com";

	public static void main(String[] args) {
		SpringApplication.run(WatsUpServer.class, args);
	}

	@GetMapping("/get_login_code")
	public ResponseEntity<String> getLoginCode(@RequestParam(value = "email") String toEmailAddress)
			throws GeneralSecurityException, MessagingException, IOException {
		// Test with this URL:  http://localhost:3000/get_login_code?email=j47ho%40uwaterloo.ca
		if (!toEmailAddress.toLowerCase().endsWith("@uwaterloo.ca")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		GMailUtils.sendEmail(FROM_EMAIL, toEmailAddress);
		return ResponseEntity.ok(String.format("Login code has been sent to %s!", toEmailAddress));
	}

	@GetMapping("/validate_login_code")
	public ResponseEntity<String>  validateLoginCode(
			@RequestParam(value = "email") String toEmailAddress,
			@RequestParam(value = "code") String code
	) throws IOException, GeneralSecurityException {
		// Test with this URL:  http://localhost:3000/validate_login_code?email=j47ho%40uwaterloo.ca&code=976947
		if (!toEmailAddress.toLowerCase().endsWith("@uwaterloo.ca")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		if (GMailUtils.validateCode(toEmailAddress, code)) {
			return ResponseEntity.ok(String.format("Validate code %s for email %s!", code, toEmailAddress));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(String
					.format("Invalid code %s for email %s!", code, toEmailAddress));
		}
	}
}
