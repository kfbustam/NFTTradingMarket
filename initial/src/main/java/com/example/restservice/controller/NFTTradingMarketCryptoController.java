package com.example.restservice.controller;

import com.example.restservice.*;
import com.example.restservice.crypto.CryptoType;
import com.example.restservice.nft.NFT;
import com.example.restservice.nft.NftService;
import com.example.restservice.nft.NftTransaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The type Airline reservation system rest controller.
 */
@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NFTTradingMarketCryptoController {

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

    @PostMapping(value = "/wallet/{wallet_id}/withdraw", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> withdrawWallet(
            @PathVariable(name="wallet_id", required=true) String walletId,
            @RequestParam(name="amount", required=true) @NotNull BigDecimal amount
            ) {

        HttpHeaders responseHeaders = new HttpHeaders();

        try {

            // todo add authentication to this endpoint

           Optional<Wallet> wallet = service.getWalletByID(walletId);

           JSONObject walletObject = null;
           if (wallet.isPresent()) {
               Wallet updatedWallet = service.makeWithdrawal(wallet.get(), amount);
               walletObject = new JSONObject()
                       .put("id", updatedWallet.getId())
                       .put("status", "withdrawal_success")
                       .put("type", updatedWallet.getType())
                       .put("new_balance", String.valueOf(updatedWallet.getCryptoBalance()));
            } else {
               return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": Wallet does not exist.}}", HttpStatus.BAD_REQUEST);
           }

            ResponseEntity<String> res = new ResponseEntity<String>(
                   walletObject.toString(),
                    responseHeaders,
                    200
            );

            return res;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            System.out.println(sw.toString());
            return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": " + ex.getMessage() +"}}", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/wallet/{wallet_id}/deposit", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> depositWallet(
            @PathVariable(name="wallet_id", required=true) String walletId,
            @RequestParam(name="amount", required=true) @NotNull BigDecimal amount
    ) {

        HttpHeaders responseHeaders = new HttpHeaders();

        try {

            // todo add authentication to this endpoint

            Optional<Wallet> wallet = service.getWalletByID(walletId);

            JSONObject walletObject = null;
            if (wallet.isPresent()) {
                Wallet updatedWallet = service.makeDeposit(wallet.get(), amount);
                walletObject = new JSONObject()
                        .put("id", updatedWallet.getId())
                        .put("status", "deposit_success")
                        .put("type", updatedWallet.getType())
                        .put("new_balance", String.valueOf(updatedWallet.getCryptoBalance()));
            } else {
                return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": Wallet does not exist.}}", HttpStatus.BAD_REQUEST);
            }

            ResponseEntity<String> res = new ResponseEntity<String>(
                    walletObject.toString(),
                    responseHeaders,
                    200
            );

            return res;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            System.out.println(sw.toString());
            return new ResponseEntity<String>("{\"BadRequest\": {\"code\": \" 400 \",\"msg\": " + ex.getMessage() +"}}", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transactions")
    @ResponseBody
    public ResponseEntity<List<NftTransaction>> getTransactions(
            @RequestParam(name="token", required=true) @NotNull String token
    ) {

        HttpHeaders responseHeaders = new HttpHeaders();

        try {

            Optional<SessionToken> optionalSession = service.getSessionByToken(token);

            if (optionalSession.isEmpty()) {
                return new ResponseEntity<List<NftTransaction>>(List.of(), HttpStatus.UNAUTHORIZED);
            }

            User user = service.getSessionByToken(token).orElseThrow().getUser();
            List<NftTransaction> transactions = nftService.getTransactions(user)
                            .stream().collect(Collectors.toList());


            ResponseEntity<List<NftTransaction>> res = new ResponseEntity<List<NftTransaction>>(
                    transactions,
                    responseHeaders,
                    200
            );

            return res;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            System.out.println(sw.toString());
            return new ResponseEntity<List<NftTransaction>>(List.of(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transactions")
    @ResponseBody
    public ResponseEntity<List<Listing>> getListings(
    ) {

        HttpHeaders responseHeaders = new HttpHeaders();

        try {

            List<Listing> transactions = service.getAllListings()
                    .stream().collect(Collectors.toList());


            ResponseEntity<List<Listing>> res = new ResponseEntity<List<Listing>>(
                    transactions,
                    responseHeaders,
                    200
            );

            return res;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            System.out.println(sw.toString());
            return new ResponseEntity<List<Listing>>(List.of(), HttpStatus.BAD_REQUEST);
        }
    }
}
