package com.example.restservice.controller;

import com.example.restservice.*;
import com.example.restservice.crypto.CryptoType;
import com.example.restservice.nft.NFT;
import com.example.restservice.nft.NftService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.context.MessageSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * The type Airline reservation system rest controller.
 */
@RestController
@Validated
public class NFTTradingMarketRESTController {

	/**
	 * The App url.
	 */
	public String appURL = "http://localhost:8080";

    @Autowired
    private Service service;

    @Autowired
    private NftService nftService;

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
            @RequestParam(name = "email", required = true) String email,
            @RequestParam(name = "password", required = true) String password
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
            return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": " + stackTrace + "}}", HttpStatus.BAD_REQUEST);
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
	@PostMapping(value = "/signup", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> signUp(
            @RequestParam(name = "email", required = true) String email,
            @RequestParam(name = "password", required = true) String password,
            @RequestParam(name = "firstname", required = true) String firstname,
            @RequestParam(name = "lastname", required = true) String lastname,
            @RequestParam(name = "nickname", required = true) String nickname
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
            return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": " + sw.toString() + "}}", HttpStatus.BAD_REQUEST);
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
	 * Create wallet
	 *
	 */
	@PostMapping("/wallet")
	@ResponseBody
	public ResponseEntity<String> createWallet(
			@RequestParam(name="token", required=true) String token,
			@RequestParam(name="type", required=true) String type
			) {

		HttpHeaders responseHeaders = new HttpHeaders();

		try {

			Optional<SessionToken> optionalSession = service.getSessionById(token);

			if (optionalSession.isEmpty()) {
				optionalSession = service.getSessionByToken(token);
			}

			optionalSession.ifPresent(session -> {
				User user = session.getUser();
				if (type == "eth") {
					service.createWallet(user, CryptoType.ETHEREUM);
				} else if (type == "btc") {
					service.createWallet(user, CryptoType.BITCOIN);
				}
	});
			
			JSONObject json = new JSONObject()
				.put("type", type);

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
	 * Get wallets
	 *
	 */
	@GetMapping("/wallets")
	@ResponseBody
	public ResponseEntity<String> wallets(
			@RequestParam(name="token", required=true) String token
			) {

		HttpHeaders responseHeaders = new HttpHeaders();

		try {

			Optional<SessionToken> optionalSession = service.getSessionById(token);

			if (optionalSession.isEmpty()) {
				optionalSession = service.getSessionByToken(token);
			}


			if (optionalSession.isEmpty()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Token expired. Please login again.\"}}", HttpStatus.BAD_REQUEST);
			}

			ArrayList<Wallet> wallets = new ArrayList<Wallet>();
			optionalSession.ifPresent((session) -> {
				List<Wallet> userWallet = service.getUserWallets(session.getUser());
				for (int i=0; i<userWallet.size(); i++) {
					wallets.add(userWallet.get(i));
				}
			});

			ArrayList<JSONObject> listOfWallets = new ArrayList<JSONObject>();
			for (int i=0; i<wallets.size(); i++) {
				listOfWallets.add(
					new JSONObject()
						.put("id", wallets.get(i).getId())
				);
			}

			ResponseEntity<String> res = new ResponseEntity<String>(
					(new JSONArray(listOfWallets)).toString(),
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
	 * Get wallet
	 *
	 */
	@GetMapping("/wallet/{id}")
	@ResponseBody
	public ResponseEntity<String> wallet(
			@RequestParam(name="token", required=true) String token,
			@RequestParam(name="id", required=true) String id
			) {
				
		HttpHeaders responseHeaders = new HttpHeaders();

		try {

			Optional<SessionToken> optionalSession = service.getSessionById(token);

			if (optionalSession.isEmpty()) {
				optionalSession = service.getSessionByToken(token);
			}


			if (optionalSession.isEmpty()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Token expired. Please login again.\"}}", HttpStatus.BAD_REQUEST);
			}

			ArrayList<CryptographicAsset> walletContents = new ArrayList<CryptographicAsset>();
			optionalSession.ifPresent((session) -> {

				Optional<Wallet> optionalWallet = service.getWalletByID(id);

				optionalWallet.ifPresent((wallet) -> {
					List<CryptographicAsset> contentsFromWallet = service.getWalletContents(wallet);
					for (int i=0; i<contentsFromWallet.size(); i++) {
						walletContents.add(contentsFromWallet.get(i));
					}
				});
			});

			ArrayList<JSONObject> listOfWalletContents = new ArrayList<JSONObject>();
			for (int i=0; i<walletContents.size(); i++) {
				listOfWalletContents.add(
					new JSONObject()
						.put("id", walletContents.get(i).getId())
						.put("img", walletContents.get(i).getImageUrl())
						.put("title", walletContents.get(i).getName())
						.put("description", walletContents.get(i).getDescription())
				);
			}

			ResponseEntity<String> res = new ResponseEntity<String>(
					(new JSONArray(listOfWalletContents)).toString(),
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
	 * Create nft response entity.
	 *
	 * @param email       the email
	 * @param file        the file
	 * @param name        the name
	 * @param description the description
	 * @param type        the type
	 * @return the response entity
	 */
	@PostMapping(value = "/nft/create", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> createNft(
            @RequestParam(name = "email", required = true) @NotEmpty String email,
            @RequestParam(name = "nft_image", required = true) @NotNull MultipartFile file,
            @RequestParam(name = "name", required = true) @NotEmpty String name,
			@RequestParam(name = "wallet_id", required = true) @NotEmpty String walletId,
			@RequestParam(name = "description", required = true) @NotNull String description,
            @RequestParam(name = "price", required = true) @NotNull double price,
            CryptoType type) {

        HttpHeaders responseHeaders = new HttpHeaders();

        try {
            // get user's wallet ID based on email and NFT Type
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get("tmp", file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());

            NFT nft = nftService.createNft(fileNameAndPath, type, walletId, name, description, price);

            JSONObject json = new JSONObject()
                    .put("name", nft.getName())
					.put("wallet_id", nft.getWallet().getId())
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
            return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": " + ex.getMessage() + "}}", HttpStatus.BAD_REQUEST);
        }
    }

	@GetMapping("/nft")
	@ResponseBody
	public ResponseEntity<List<NFT>> nfts() {

		HttpHeaders responseHeaders = new HttpHeaders();

		try {

			List<NFT> allNfts = nftService.getAllNfts();

			ResponseEntity<List<NFT>> res = new ResponseEntity<List<NFT>>(
					allNfts,
					responseHeaders,
					200
			);

			return res;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			System.out.println(sw.toString());
			return new ResponseEntity<List<NFT>>(List.of(), HttpStatus.BAD_REQUEST);
		}
	}
}
