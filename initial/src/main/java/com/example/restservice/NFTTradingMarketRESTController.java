package com.example.restservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.ResponseEntity;
		import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.context.MessageSource;

import org.json.JSONObject;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * The type Airline reservation system rest controller.
 */
@RestController
public class NFTTradingMarketRESTController {

	public String appURL = "http://localhost:8080";

	@Autowired
	private Service service;

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * Sign in user.
	 *
	 */
	@PostMapping("/signin")
	@ResponseBody
	public ResponseEntity<String> signIn(
		@RequestParam(name="email", required=true) String email,
		@RequestParam(name="password", required=true) String password
	) {
		HttpHeaders responseHeaders = new HttpHeaders();

		try {
			Optional<User> optionalUser = service.findUserByEmail(email);

			if (optionalUser == null) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"User not found.\"}}", HttpStatus.BAD_REQUEST);
			}

			User userFound = optionalUser.map(
				user -> user
			).orElse(null);

			JSONObject json = new JSONObject()
				.put("email", userFound.getEmail());

			ResponseEntity<String> res = new ResponseEntity<String>(
				json.toString(),
				responseHeaders,
				200
			);

	  return res;
		} catch (Exception ex) {
			return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": " + ex.getMessage() +"}}", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Sign up user.
	 *
	 */
	@PostMapping("/signup")
	@ResponseBody
	public ResponseEntity<String> signUp(
		@RequestParam(name="email", required=true) String email,
		@RequestParam(name="password", required=true) String password,
		@RequestParam(name="firstname", required=true) String firstname,
		@RequestParam(name="lastname", required=true) String lastname,
		@RequestParam(name="nickname", required=true) String nickname
	) {
		HttpHeaders responseHeaders = new HttpHeaders();

		try {
			Optional<User> optionalUser = service.findUserByEmail(email);

			if (optionalUser != null) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"another User with the same email already exists.\"}}", HttpStatus.BAD_REQUEST);
			}

			User user = service.createUser(email, password, firstname, lastname, nickname);

			String token = UUID.randomUUID().toString();
			service.createVerificationToken(user, token);
			
			String recipientAddress = user.getEmail();
			String subject = "Registration Confirmation";
			String confirmationUrl = this.appURL + "/registrationConfirm?token=" + token;
			
			SimpleMailMessage emailMessage = new SimpleMailMessage();
			emailMessage.setTo(recipientAddress);
			emailMessage.setSubject(subject);
			emailMessage.setText("http://localhost:8080" + confirmationUrl);
			mailSender.send(emailMessage);

			JSONObject json = new JSONObject()
				.put("email", user.getEmail())
				.put("firstname", user.getFirstName())
				.put("lastname", user.getLastName())
				.put("nickname", user.getNickName());

			ResponseEntity<String> res = new ResponseEntity<String>(
				json.toString(),
				responseHeaders,
				200
			);

	  return res;
		} catch (Exception ex) {
			return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": " + ex.getMessage() +"}}", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Verify user.
	 *
	 */
	@PostMapping("/registrationConfirm")
	public RedirectView registrationConfirm(
		@RequestParam(name="token", required=true) String token
	) {
		HttpHeaders responseHeaders = new HttpHeaders();

		try {
			Optional<VerificationToken> optionalVerificationToken = service.findByToken(token);

			if (optionalVerificationToken == null) {
				return new RedirectView(this.appURL + "?error=TokenNotFound");
			}

			VerificationToken tokenFound = optionalVerificationToken.map(
				user -> user
			).orElse(null);

			User user = tokenFound.getUser();
			user.setVerified();
			service.updateUser(user);

			return new RedirectView(this.appURL);
		} catch (Exception ex) {
			return new RedirectView(this.appURL + "?error=" + ex.getMessage());
		}
	}
}
