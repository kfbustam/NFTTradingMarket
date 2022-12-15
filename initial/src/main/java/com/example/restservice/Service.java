package com.example.restservice;

import com.example.restservice.crypto.CryptoType;
import com.example.restservice.nft.NFT;
import com.example.restservice.nft.NFTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class Service {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private NFTRepository nftRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private SessionTokenRepository sessionRepository;

    public Optional<User> findUser(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<VerificationToken> findByToken(String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken);
    }

    public User createUser(String email, String password, String firstname, String lastname, String nickname)
            throws Exception {
        try {
            User newUser = new User(email, password, firstname, lastname, nickname);
            User userResponse = userRepository.saveAndFlush(newUser);
            return userResponse;
        } catch (Exception e) {
            throw e;
        }
    }

    public Wallet createWallet(User user, CryptoType type) {
        Wallet newWallet = new Wallet(user, type);
        Wallet walletCreated = walletRepository.saveAndFlush(newWallet);
        return walletCreated;
    }

    public void moveNFT(Wallet toWallet, NFT nft) {
        nft.setWallet(toWallet);
        nftRepository.save(nft);
    }

    public void deleteListingForNFT(NFT nft) {
        listingRepository.deleteListingByNFTID(nft.getId());
    }

    public ArrayList<Wallet> getUserWallets(User user) {
        Collection<Wallet> usersWallets = walletRepository.findUserWallet(user.getID());
        return new ArrayList<>(usersWallets);
    }

    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    public Optional<Wallet> getUserWallet(Wallet wallet) {
        Optional<Wallet> userWallet = walletRepository.findById(wallet.getId());
        return userWallet;
    }

    public Wallet findUsersWalletByType(User user, CryptoType type) throws Exception {
        ArrayList<Wallet> userWallet = new ArrayList<Wallet>(walletRepository.findUserWalletByType(user.getID(), type));
        if (userWallet.size() > 1) {
            throw new Exception("Should not have more than one wallet for a specific type");
        }
        return userWallet.get(0);
    }

    public Collection<NFT> getNFTsInWallet(Wallet wallet) {
        return nftRepository.findByWalletID(wallet.getId());
    }

    public double getNFTsTotalPrice(Wallet wallet) {
        return nftRepository.findTotalPriceInWallet(wallet.getId());
    }

    public Optional<SessionToken> getSessionByToken(String token) {
        Optional<SessionToken> userSession = sessionRepository.findByToken(token);
        return userSession;
    }

    public Optional<SessionToken> getSessionById(String id) {
        Optional<SessionToken> userSession = sessionRepository.findById(id);
        return userSession;
    }

    public Optional<Wallet> getWalletByID(String id) {
        return walletRepository.findById(id);
    }

    public Wallet makeWithdrawal(Wallet wallet, BigDecimal amount) throws Exception {
        if (wallet.getCryptoBalance().compareTo(amount) >= 0) {
            // update new balance
            wallet.setCryptoBalance(wallet.getCryptoBalance().subtract(amount));
            walletRepository.save(wallet);
        } else {
            throw new Exception("Wallet balance is not sufficient for withdrawal.");
        }
        return wallet;
    }

    public Wallet makeDeposit(Wallet wallet, BigDecimal amount) throws Exception {
        // update new balance
        // todo any other validation needed
        wallet.setCryptoBalance(wallet.getCryptoBalance().add(amount));
        walletRepository.save(wallet);
        return wallet;
    }

    public void updateUserWalletBalance(Wallet wallet, Long newBalance) {
        wallet.setCryptoBalance(new BigDecimal(newBalance));
        walletRepository.saveAndFlush(wallet);
    }

    public void createOffer(User user, Double price, NFT nft) {
        Offer offer = new Offer(user, price, nft);
        offerRepository.saveAndFlush(offer);
    }
}
