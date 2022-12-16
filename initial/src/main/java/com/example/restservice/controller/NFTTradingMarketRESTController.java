package com.example.restservice.controller;

import com.example.restservice.*;
import com.example.restservice.crypto.CryptoType;
import com.example.restservice.nft.NFT;
import com.example.restservice.nft.NftCategory;
import com.example.restservice.nft.NftService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.context.MessageSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.activation.FileTypeMap;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.stream.Collectors;

/**
 * The type Airline reservation system rest controller.
 */
@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NFTTradingMarketRESTController {

	/**
	 * The App url.
	 */
	public String appURL = "http://localhost:3000";

    @Autowired
    private Service service;

    @Autowired
    private NftService nftService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

	@GetMapping("images")
	public ResponseEntity<byte[]> getImage(
			@RequestParam(name="image_name", required=true) @NotNull String image_name
	) throws IOException {
		File img = new File("tmp/" + image_name);
		return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img)))
				.body(Files.readAllBytes(img.toPath()));
	}

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

            User userFound = optionalUser.orElseThrow();

            if (!userFound.isVerified()) {
                return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"User is not yet verified. Please check your email for the verification link.\"}}", HttpStatus.BAD_REQUEST);
            }

			SessionToken sessionToken = service.createSessionToken(userFound, UUID.randomUUID().toString());

            JSONObject json = new JSONObject()
                    .put("email", userFound.getEmail())
					.put("token", sessionToken.getToken());

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
            return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 500 \",\"msg\": " + ex.getMessage() + "}}", HttpStatus.INTERNAL_SERVER_ERROR);
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
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "firstname", required = true) String firstname,
            @RequestParam(name = "lastname", required = true) String lastname,
            @RequestParam(name = "nickname", required = true) String nickname,
			@RequestParam(name = "type", required = true) NftUserType type,
			@RequestParam(name = "social_token", required = false) String socialToken


	) {
        HttpHeaders responseHeaders = new HttpHeaders();

        try {
            Optional<User> optionalUser = service.findUserByEmail(email);

            if (optionalUser.isPresent()) {
                return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Another user with the same email already exists.\"}}", HttpStatus.BAD_REQUEST);
            }

			if (type == NftUserType.LOCAL && password == "") {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Missing password\"}}", HttpStatus.BAD_REQUEST);
			}

			if (password == "") {
				password = "token";
			}

            User user = service.createUser(email, password, firstname, lastname, nickname, type);
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

			service.createSessionToken(user, socialToken);

			service.createWallet(user, CryptoType.ETHEREUM);

			service.createWallet(user, CryptoType.BITCOIN);

            return res;
        } catch (Exception ex) {
			String message = ex.getMessage();

			if (ex.getClass() == DataIntegrityViolationException.class) {
				message = ex.getCause().getCause().getMessage();
			}
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            System.out.println(sw.toString());
          	return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 500 \",\"msg\": " + ex.getMessage() + "}}", HttpStatus.INTERNAL_SERVER_ERROR);
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

            VerificationToken tokenFound = optionalVerificationToken.orElseThrow();

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

			Optional<SessionToken> optionalSession = service.getSessionByToken(token);

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
	@GetMapping(value = "/wallets", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> wallets(
			@RequestParam(name="token", required=true) String token
			) {

		HttpHeaders responseHeaders = new HttpHeaders();

		try {

			Optional<SessionToken> optionalSession = service.getSessionByToken(token);

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
							.put("type", wallets.get(i).getType())
							.put("balance", String.valueOf(wallets.get(i).getCryptoBalance()))
				);
			}

			ResponseEntity<String> res = new ResponseEntity<String>(
					listOfWallets.toString(),
					responseHeaders,
					200
			);

			return res;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			System.out.println(sw.toString());
			return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 500 \",\"msg\": " + ex.getMessage() + "}}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Get wallet
	 *
	 */
	@PostMapping("/wallet/{id}")
	@ResponseBody
	public ResponseEntity<String> wallet(
			@RequestParam(name="token", required=true) String token,
			@RequestParam(name="id", required=true) String id
			) {
				
		HttpHeaders responseHeaders = new HttpHeaders();

		try {

			Optional<SessionToken> optionalSession = service.getSessionByToken(token);

			if (optionalSession.isEmpty()) {
				optionalSession = service.getSessionByToken(token);
			}


			if (optionalSession.isEmpty()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Token expired. Please login again.\"}}", HttpStatus.BAD_REQUEST);
			}

			ArrayList<NFT> allNFTs = new ArrayList<NFT>();
			HashMap<String, Double> allCryptoTotalHashMap = new HashMap<>();
			optionalSession.ifPresent((session) -> {

				Optional<Wallet> optionalWallet = service.getWalletByID(id);

				optionalWallet.ifPresent((wallet) -> {
					service.getNFTsInWallet(wallet).forEach(nft -> allNFTs.add(nft));
				});
			});

			ArrayList<JSONObject> listOfWalletContents = new ArrayList<JSONObject>();
			for (int i=0; i<allNFTs.size(); i++) {
				listOfWalletContents.add(
					new JSONObject()
						.put("id", allNFTs.get(i).getId())
						.put("img", allNFTs.get(i).getImageUrl())
						.put("title", allNFTs.get(i).getName())
						.put("description", allNFTs.get(i).getDescription())
						.put("price", allNFTs.get(i).getPrice())
				);
			}

			allCryptoTotalHashMap.forEach((key, value) -> {
				listOfWalletContents.add(
					new JSONObject()
						.put("title", key)
						.put("price", value)
				);
			});

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
			return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 500 \",\"msg\": " + ex.getMessage() + "}}", HttpStatus.INTERNAL_SERVER_ERROR);
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
			@RequestPart("nft_image") MultipartFile file,
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

            NFT nft = nftService.createNft(fileNameAndPath.getFileName(), type, walletId, name, description, price, NftCategory.Anime);

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
            return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 500 \",\"msg\": " + ex.getMessage() + "}}", HttpStatus.INTERNAL_SERVER_ERROR);
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

	@PostMapping("/nft/buy")
	@ResponseBody
	public ResponseEntity<String> buyNFT(
		@RequestParam(name="token", required=true) String token,
		@RequestParam(name = "nftID", required = true) @NotEmpty String nftID,
		@RequestParam(name = "sellerID", required = true) @NotEmpty String sellerID
	) {

		HttpHeaders responseHeaders = new HttpHeaders();
		try {

			Optional<SessionToken> optionalSession = service.getSessionByToken(token);

			if (optionalSession.isEmpty()) {
				optionalSession = service.getSessionByToken(token);
			}

			if (optionalSession.isEmpty()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Token expired. Please login again.\"}}", HttpStatus.BAD_REQUEST);
			}
			NFT nft = nftService.getNFT(nftID).orElseThrow();

			User buyer = service.getSessionByToken(token).orElseThrow().getUser();

			User seller = service.findUser(sellerID).orElseThrow();

			Wallet buyerWallet;
			Wallet sellerWallet;
			if (nft.getNftType() == CryptoType.BITCOIN) {			
				buyerWallet = service.findUsersWalletByType(buyer, CryptoType.BITCOIN);
				sellerWallet = service.findUsersWalletByType(seller, CryptoType.BITCOIN);
			} else {
				buyerWallet = service.findUsersWalletByType(buyer, CryptoType.ETHEREUM);
				sellerWallet = service.findUsersWalletByType(seller, CryptoType.ETHEREUM);
			}

			BigDecimal balance = buyerWallet.getCryptoBalance();

			if (balance.longValue() < BigDecimal.valueOf(nft.getPrice()).longValue()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"You do not have enough of this currency to proceed with this transaction.\"}}", HttpStatus.BAD_REQUEST);
			}

			service.updateUserWalletBalance(buyerWallet, balance.longValue()-BigDecimal.valueOf(nft.getPrice()).longValue());

			service.updateUserWalletBalance(sellerWallet, balance.longValue()+BigDecimal.valueOf(nft.getPrice()).longValue());

			service.deleteListingForNFT(nft);

					// TODO: Make sure to uncomment (running into key constraint on listing_id)
			// service.moveNFT(buyerWallet, nft);

			JSONObject json = new JSONObject()
						.put("nftID", nft.getId())
						.put("cryptoType", nft.getNftType());

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
			return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 500 \",\"msg\": " + ex.getMessage() + "}}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/nft/auction/offer")
	@ResponseBody
	public ResponseEntity<String> auctionOfferNFT(
		@RequestParam(name="token", required=true) String token,
		@RequestParam(name = "offerPrice", required = true) @NotEmpty Double offerPrice,
		@RequestParam(name = "nftID", required = true) @NotEmpty String nftID
	) {

		HttpHeaders responseHeaders = new HttpHeaders();
		try {


			Optional<SessionToken> optionalSession = service.getSessionByToken(token);

			if (optionalSession.isEmpty()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Token expired. Please login again.\"}}", HttpStatus.BAD_REQUEST);
			}

			User buyer = service.getSessionByToken(token).orElseThrow().getUser();
			NFT nft = nftService.getNFT(nftID).orElseThrow();
			
			Wallet buyerWallet;
			if (nft.getNftType() == CryptoType.BITCOIN) {			
				buyerWallet = service.findUsersWalletByType(buyer, CryptoType.BITCOIN);
			} else {
				buyerWallet = service.findUsersWalletByType(buyer, CryptoType.ETHEREUM);
			}

			if (buyerWallet.getCryptoBalance().longValue() < nft.getPrice()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"You do not have enough of this currency to proceed with this transaction.\"}}", HttpStatus.BAD_REQUEST);
			}

			service.createOffer(buyer, offerPrice, nft);
			
			JSONObject json = new JSONObject()
						.put("nftID", nft.getId());

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
			return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 500 \",\"msg\": " + ex.getMessage() + "}}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
		GET User's Own NFT listings by Token
	 */
	@PostMapping("/nft/listings")
	@ResponseBody
	public ResponseEntity<String> listings(
		@RequestParam(name="token", required=true) String token
	) {

		HttpHeaders responseHeaders = new HttpHeaders();
		try {


			Optional<SessionToken> optionalSession = service.getSessionByToken(token);

			if (optionalSession.isEmpty()) {
				optionalSession = service.getSessionByToken(token);
			}

			if (optionalSession.isEmpty()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Token expired. Please login again.\"}}", HttpStatus.BAD_REQUEST);
			}

			User buyer = service.getSessionByToken(token).orElseThrow().getUser();

			List<NFT> listings = service.getAllListingsAsNFTs().stream().filter(x -> x.getWallet().getUser().getID().equals(buyer.getID())).collect(Collectors.toList());
			
			ArrayList<JSONObject> json = new ArrayList<>();
			listings.forEach(listing -> {
				json.add(
					new JSONObject()
						.put("price", listing.getPrice())
						.put("description", listing.getDescription())
						.put("assetURL", listing.getAssetUrl())
						.put("imageURL", listing.getImageUrl())
						.put("name", listing.getName())
						.put("type", listing.getNftType())
						.put("lastRecordedTime", listing.getLastRecordedTime())
						.put("address", listing.getSmartContractAddress())
				);
			});

			ResponseEntity<String> res = new ResponseEntity<String>(
				new JSONArray(json).toString(),
					responseHeaders,
					200
			);

			return res;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			System.out.println(sw.toString());
			return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 500 \",\"msg\": " + ex.getMessage() + "}}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/listings")
	@ResponseBody
	public ResponseEntity<String> allListings(
	) {

		HttpHeaders responseHeaders = new HttpHeaders();
		try {

			List<NFT> listings = service.getAllListingsAsNFTs();

			ArrayList<JSONObject> json = new ArrayList<>();
			listings.forEach(listing -> {
				json.add(
						new JSONObject()
								.put("nftId", listing.getId())
								.put("description", listing.getDescription())
								.put("assetURL", listing.getAssetUrl())
								.put("imageURL", listing.getImageUrl())
								.put("name", listing.getName())
								.put("nftType", listing.getNftType())
								.put("saleType", listing.getListing().getType())
								.put("sellerId", listing.getListing().getSeller().getID())
								.put("lastRecordedTime", listing.getLastRecordedTime())
								.put("smartContractAddress", listing.getSmartContractAddress())
				);
			});

			ResponseEntity<String> res = new ResponseEntity<String>(
					new JSONArray(json).toString(),
					responseHeaders,
					200
			);

			return res;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			System.out.println(sw.toString());
			return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 500 \",\"msg\": " + ex.getMessage() + "}}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
