package com.example.restservice;

import com.example.restservice.nft.NftType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.context.MessageSource;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * The type Airline reservation system rest controller.
 */
@RestController
public class NFTTradingMarketRESTController {

	/**
	 * The App url.
	 */
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
	 * @param email    the email
	 * @param password the password
	 * @return the response entity
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

			if (optionalUser.isEmpty()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"User not found.\"}}", HttpStatus.BAD_REQUEST);
			}

			User userFound = optionalUser.map(
				user -> user
			).orElseThrow();

			if (!userFound.isVerified()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"User is not yet verified. Please check your email for the verification link.\"}}", HttpStatus.BAD_REQUEST);
			}

			JSONObject json = new JSONObject()
				.put("email", userFound.getEmail());

			ResponseEntity<String> res = new ResponseEntity<String>(
				json.toString(),
				responseHeaders,
				200
			);

	  return res;
		} catch (Exception ex) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					ex.printStackTrace(pw);
					String stackTrace = sw.toString(); // stack trace as a string
					System.out.println(sw.toString());
			return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": " + stackTrace +"}}", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Sign up user.
	 *
	 * @param email     the email
	 * @param password  the password
	 * @param firstname the firstname
	 * @param lastname  the lastname
	 * @param nickname  the nickname
	 * @return the response entity
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

   			if (optionalUser.isPresent()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Another user with the same email already exists.\"}}", HttpStatus.BAD_REQUEST);
			}

				 User user = service.createUser(email, password, firstname, lastname, nickname);
	String token = UUID.randomUUID().toString();
			service.createVerificationToken(user, token);
	String recipientAddress = user.getEmail();
			String subject = "Registration Confirmation";
			String confirmationUrl = this.appURL + "/registrationConfirm/" + token;
	SimpleMailMessage emailMessage = new SimpleMailMessage();
	emailMessage.setTo(recipientAddress);
			emailMessage.setSubject(subject);
			emailMessage.setText("\nPlease go to the following link to verify your account: \n\n" + confirmationUrl);
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
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					ex.printStackTrace(pw);
								System.out.println(sw.toString());
			return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": " + sw.toString() +"}}", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Verify user.
	 *
	 * @param token the token
	 * @return the redirect view
	 */
	@GetMapping("/registrationConfirm/{token}")
	public RedirectView registrationConfirm(
		@PathVariable String token
	) {
		HttpHeaders responseHeaders = new HttpHeaders();

		try {
			Optional<VerificationToken> optionalVerificationToken = service.findByToken(token);

			if (optionalVerificationToken.isEmpty()) {
				return new RedirectView(this.appURL + "?error=TokenNotFound");
			}

			VerificationToken tokenFound = optionalVerificationToken.map(
				user -> user
			).orElseThrow();

			User user = tokenFound.getUser();
			user.setVerified();
			service.updateUser(user);

			return new RedirectView(this.appURL + "?message=success");
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			String stackTrace = sw.toString(); // stack trace as a string
			System.out.println(sw.toString());
			return new RedirectView(this.appURL + "?error=" + ex.getMessage());
		}
	}


	/**
	 * Create nft response entity.
	 *
	 * @param email       the email
	 * @param file        the file
	 * @param name        the name
	 * @param description the description
	 * @param type        the type
	 * @return the response entity
	 */
	@PostMapping("/nft/create")
	@ResponseBody
	public ResponseEntity<String> createNft(
			@RequestParam(name="email", required=true) String email,
			@RequestParam(name="nft_image", required=true) MultipartFile file,
			@RequestParam(name="name", required=true) String name,
			@RequestParam(name="description", required=true) String description,
			@RequestParam(name="type", required=true) NftType type) {

		HttpHeaders responseHeaders = new HttpHeaders();

		try {
			// get user's wallet ID based on email and NFT Type
			StringBuilder fileNames = new StringBuilder();
			Path fileNameAndPath = Paths.get("tmp", file.getOriginalFilename());
			fileNames.append(file.getOriginalFilename());
			Files.write(fileNameAndPath, file.getBytes());

			NFT nft = new NFT();
			nft.setImageUrl(fileNameAndPath.toString());
			nft.setNftType(type);
			nft.setTokenId(UUID.randomUUID().toString());
			nft.setSmartContractAddress(UUID.randomUUID().toString());
			nft.setName(name);
			nft.setDescription(description);

			JSONObject json = new JSONObject()
					.put("name", nft.getName())
					.put("token_id", nft.getTokenId())
					.put("smart_contract_id", nft.getSmartContractAddress());


			ResponseEntity<String> res = new ResponseEntity<String>(
					json.toString(),
					responseHeaders,
					200
			);

			return res;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			System.out.println(sw.toString());
			return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": " + sw.toString() +"}}", HttpStatus.BAD_REQUEST);
		}
	}
}
