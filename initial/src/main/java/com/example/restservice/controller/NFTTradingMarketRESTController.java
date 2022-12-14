package com.example.restservice.controller;

import com.example.restservice.*;
import com.example.restservice.crypto.CryptoType;
import com.example.restservice.nft.NFT;
import com.example.restservice.nft.NftCategory;
import com.example.restservice.nft.NftService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
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

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
	public String appURL = "http://Cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com";
	public String frontendUrl = "http://surfacery.com";

    @Autowired
    private Service service;

    @Autowired
    private NftService nftService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

	@GetMapping("images")
	@Cacheable(value = "images", key = "{ #image_name }")
	public ResponseEntity<InputStreamResource> getImage(
			@RequestParam(name="image_name", required=true) @NotNull String image_name
	) throws IOException {
		InputStream inputStream = nftService.getNftImage(image_name);
		var jpg = Optional.ofNullable(image_name)
				.filter(f -> f.contains("."))
				.map(f -> f.substring(image_name.lastIndexOf(".") + 1));
		MediaType contentType = jpg.isPresent() ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;

		return ResponseEntity.ok()
				.contentType(contentType)
				.body(new InputStreamResource(inputStream));

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
	@Transactional
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

            if (optionalUser.isPresent() && type == NftUserType.LOCAL) {
                return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Another user with the same email already exists.\"}}", HttpStatus.BAD_REQUEST);
            }

			if (type == NftUserType.LOCAL && password == "") {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Missing password\"}}", HttpStatus.BAD_REQUEST);
			}

			if (password == "") {
				password = "token";
			}

			// idempotent scenario for social login
			if (optionalUser.isPresent() && type == NftUserType.GOOGLE) {
				JSONObject json = new JSONObject()
						.put("email", optionalUser.get().getEmail())
						.put("firstname", optionalUser.get().getFirstName())
						.put("lastname", optionalUser.get().getLastName())
						.put("nickname", optionalUser.get().getNickName());

				if(!optionalUser.get().isVerified()) {
					ResponseEntity<String> res = new ResponseEntity<String>(
							json.toString(),
							responseHeaders,
							201
					);
					return res;
				}
				service.createSessionToken(optionalUser.get(), socialToken);

				ResponseEntity<String> res = new ResponseEntity<String>(
						json.toString(),
						responseHeaders,
						200
				);
				return res;
			}

			// add a random suffix to social login nickname
			String generatedNickname = nickname;

			if (NftUserType.GOOGLE == type) {
				Random r = new Random( System.currentTimeMillis() );
				generatedNickname = generatedNickname.replaceAll("\\s+","")
						+ (10000 + r.nextInt(20000));
			}

			User user = service.createUser(email, password, firstname, lastname, generatedNickname, type);
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
                    .put("nickname", generatedNickname);

            ResponseEntity<String> res = new ResponseEntity<String>(
                    json.toString(),
                    responseHeaders,
                    201
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
                return new RedirectView(this.frontendUrl + "?error=TokenNotFound");
            }

            VerificationToken tokenFound = optionalVerificationToken.orElseThrow();

            User user = tokenFound.getUser();
            user.setVerified();
            service.updateUser(user);

            return new RedirectView(this.frontendUrl + "?message=success");
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String stackTrace = sw.toString(); // stack trace as a string
            System.out.println(sw.toString());
            return new RedirectView(this.frontendUrl + "?error=" + ex.getMessage());
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
	 * @param file        the file
	 * @param name        the name
	 * @param description the description
	 * @param type        the type
	 * @return the response entity
	 */
	@PostMapping(value = "/nft/create", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> createNft(
			@RequestPart("nft_image") MultipartFile file,
            @RequestParam(name = "name", required = true) @NotEmpty String name,
			@RequestParam(name = "description", required = true) @NotNull String description,
			@RequestParam(name = "token", required = true) @NotNull String token,
			CryptoType type) {

        HttpHeaders responseHeaders = new HttpHeaders();

        try {
			Optional<SessionToken> optionalSession = service.getSessionByToken(token);
			if (optionalSession.isEmpty()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Token expired. Please login again.\"}}", HttpStatus.BAD_REQUEST);
			}

			ArrayList<Wallet> wallet = service.getUserWallets(optionalSession.get().getUser());
			if (wallet.size() == 0) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"User does not have wallets.\"}}", HttpStatus.BAD_REQUEST);
			}

			String walletId = wallet.stream()
					.filter(w -> type.equals(w.getType()))
					.collect(Collectors.toList())
					.get(0).getId();

			// get user's wallet ID based on email and NFT Type
//            StringBuilder fileNames = new StringBuilder();
//            Path fileNameAndPath = Paths.get("static/tmp", file.getOriginalFilename());
//            fileNames.append(file.getOriginalFilename());
//            Files.write(fileNameAndPath, file.getBytes());

            NFT nft = nftService.createNft(file, type, walletId, name, description, NftCategory.Anime);

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

	@PostMapping(value = "/listing", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> createListing(
			@RequestParam(name = "token", required = true) @NotEmpty String token,
			@RequestParam(name = "nftId", required = true) @NotEmpty String nftId,
			@RequestParam(name = "price", required = true) @NotNull double price,
			@RequestParam(name = "saleType", required = true) @NotNull SaleType type) {

		HttpHeaders responseHeaders = new HttpHeaders();

		try {

			if (price <= 0) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Price too low.\"}}", HttpStatus.BAD_REQUEST);
			}
			Optional<SessionToken> optionalSession = service.getSessionByToken(token);

			if (optionalSession.isEmpty()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Token expired. Please login again.\"}}", HttpStatus.BAD_REQUEST);
			}

			Optional<NFT> optionalNFT = nftService.getNFT(nftId);

			if (optionalNFT.isEmpty() || optionalNFT.get().getListing() != null) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"NFT cannot be sold..\"}}", HttpStatus.BAD_REQUEST);
			}

			service.createListing(optionalNFT.get(), optionalSession.get().getUser(), price, type);

			ResponseEntity<String> res = new ResponseEntity<String>(
					"{}",
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
	@GetMapping("/user_nft_listings")
	@ResponseBody
	public ResponseEntity<String> listings(
			@RequestParam(name="token", required=true) String token,
			@RequestParam(name="type", required=false) String type
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

			List<NFT> listings = service.getAllListingsAsNFTs()
					.stream()
					.filter(x -> x.getWallet().getUser().getID().equals(buyer.getID()))
					.collect(Collectors.toList());

			ArrayList<JSONObject> json = new ArrayList<>();
			listings.forEach(listing -> {
				json.add(
						new JSONObject()
								.put("price", listing.getListing().getListingPrice())
								.put("description", listing.getDescription())
								.put("assetURL", listing.getAssetUrl())
								.put("imageURL", listing.getImageUrl())
								.put("name", listing.getName())
								.put("type", listing.getNftType())
								.put("status", listing.getListing().getStatus())
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

	@GetMapping("/nft/{token}")
	@ResponseBody
	public ResponseEntity<List<NFT>> getUserNfts(
			@RequestParam(name="token", required=true) String token

	) {
		HttpHeaders responseHeaders = new HttpHeaders();

		Optional<SessionToken> optionalSession = service.getSessionByToken(token);

		if (optionalSession.isEmpty()) {
			return new ResponseEntity<List<NFT>>(List.of(), HttpStatus.BAD_REQUEST);
		}

		User user = service.getSessionByToken(token).orElseThrow().getUser();

		try {
			List<NFT> allNfts = nftService.getAllNftsByUser(user);

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
	@Transactional
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

			if (buyer.getID().equals(seller.getID())) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Cannot buy own NFT.\"}}", HttpStatus.BAD_REQUEST);
			}

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

			assert nft.getListing() != null;
			if (balance.compareTo(nft.getListing().getListingPrice()) < 0) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"You do not have enough of this currency to proceed with this transaction.\"}}", HttpStatus.BAD_REQUEST);
			}

			service.updateUserWalletBalance(buyerWallet, balance.subtract(nft.getListing().getListingPrice()));

			service.updateUserWalletBalance(sellerWallet, balance.add(nft.getListing().getListingPrice()));

			service.deleteListingForNFT(nft);

			service.createNftTrancsaction(buyer, seller, nft, nft.getNftType(),new Date(), nft.getListing().getListingPrice(),balance.subtract(nft.getListing().getListingPrice()));

			service.moveNFT(buyerWallet, nft);

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

	@PostMapping("/nft/auction/offer/cancel")
	@ResponseBody
	public ResponseEntity<String> buyNFT(
		@RequestParam(name="token", required=true) String token,
		@RequestParam(name = "nftID", required = true) @NotEmpty String nftID
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

			service.deleteAllOffersByAuthorOnNft(buyer, nft);

			ResponseEntity<String> res = new ResponseEntity<String>(
				"Successfully deleted",
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
		@RequestParam(name = "offerPrice", required = true) Double offerPrice,
		@RequestParam(name = "nftID", required = true) String nftID
	) {

		HttpHeaders responseHeaders = new HttpHeaders();
		try {

			Optional<SessionToken> optionalSession = service.getSessionByToken(token);

			if (optionalSession.isEmpty()) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Token expired. Please login again.\"}}", HttpStatus.BAD_REQUEST);
			}

			User buyer = service.getSessionByToken(token).orElseThrow().getUser();
			NFT nft = nftService.getNFT(nftID).orElseThrow();

			if (buyer.getID().equals(nft.getWallet().getUser().getID())) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"Cannot buy own NFT.\"}}", HttpStatus.BAD_REQUEST);
			}

			if ((new Date()).compareTo(nft.getListing().getExpirationTime()) > 0) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"This auction is done\"}}", HttpStatus.BAD_REQUEST);
			}
			
			Double minimumPrice = service.getMinimumOfferPrice(nftID);
				if (minimumPrice == null) {
					minimumPrice = 0.0;
				}
			if (offerPrice <= minimumPrice) {
				return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": \"You must offer a price higher than: "+minimumPrice.toString()+"\"}}", HttpStatus.BAD_REQUEST);
			}

			Wallet buyerWallet;
			if (nft.getNftType() == CryptoType.BITCOIN) {			
				buyerWallet = service.findUsersWalletByType(buyer, CryptoType.BITCOIN);
			} else {
				buyerWallet = service.findUsersWalletByType(buyer, CryptoType.ETHEREUM);
			}

			if (buyerWallet.getCryptoBalance().compareTo(nft.getListing().getListingPrice()) < 0) {
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
		GET User's Own NFT offers by nftID
	 */
	@GetMapping("/nft/auction/offers")
	@ResponseBody
	public ResponseEntity<String> nftOffers(
		@RequestParam(name="token", required=true) String token,
				@RequestParam(name = "nftID", required = true) @NotEmpty String nftID
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

			List<Offer> offerHistory = service.getOfferHistory(nftID);

			var userId = optionalSession.get().getUser().getID();

			offerHistory = offerHistory.stream()
					.filter(x -> x.getUser().getID().equals(userId))
					.collect(Collectors.toList());
			
			ArrayList<JSONObject> json = new ArrayList<>();
			offerHistory.forEach(offer -> {
						User userThatMadeOffer = offer.getUser();
				json.add(
					new JSONObject()
						.put("price", offer.getOfferPrice())
						.put("time", offer.getCreatedDate())
						.put("user", 
									new JSONObject()
									.put("id", userThatMadeOffer.getID())
									.put("firstName", userThatMadeOffer.getFirstName())
									.put("lastName", userThatMadeOffer.getLastName())
									.put("nickName", userThatMadeOffer.getNickName())
						)
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

	@GetMapping("/nft/auction/offers/all")
	@ResponseBody
	public ResponseEntity<String> nftOffers(
			@RequestParam(name = "nftID", required = true) @NotEmpty String nftID
	) {
		HttpHeaders responseHeaders = new HttpHeaders();
		try {

			ArrayList<Offer> offerHistory = service.getOfferHistory(nftID);

			ArrayList<JSONObject> json = new ArrayList<>();
			offerHistory.forEach(offer -> {
				User userThatMadeOffer = offer.getUser();
				json.add(
						new JSONObject()
								.put("price", offer.getOfferPrice())
								.put("time", offer.getCreatedDate())
								.put("user",
										new JSONObject()
												.put("id", userThatMadeOffer.getID())
												.put("firstName", userThatMadeOffer.getFirstName())
												.put("lastName", userThatMadeOffer.getLastName())
												.put("nickName", userThatMadeOffer.getNickName())
								)
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
				Double minimumPrice = service.getMinimumOfferPrice(listing.getId());

				var childJson =
						new JSONObject()
								.put("nftId", listing.getId())
								.put("minimumPrice", minimumPrice)
								.put("price", listing.getListing().getListingPrice())
								.put("description", listing.getDescription())
								.put("assetURL", listing.getAssetUrl())
								.put("imageURL", listing.getImageUrl())
								.put("name", listing.getName())
								.put("tags", listing.getCategory().toString())
								.put("nftType", listing.getNftType())
								.put("saleType", listing.getListing().getType())
								.put("seller", 
									new JSONObject()
									.put("name", listing.getListing().getSeller().getNickName())
											.put("id", listing.getListing().getSeller().getID())
								)
								.put("lastRecordedTime", listing.getLastRecordedTime())
								.put("smartContractAddress", listing.getSmartContractAddress());
				if(listing.getListing().getType().equals(SaleType.AUCTION)
					&& listing.getListing().getExpirationTime() != null) {
					childJson.put("expirationTime", listing.getListing().getExpirationTime().getTime());
				}
				json.add(childJson);
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
