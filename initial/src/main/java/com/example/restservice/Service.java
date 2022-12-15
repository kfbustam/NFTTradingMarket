package com.example.restservice;

import com.example.restservice.crypto.CryptoType;
import com.example.restservice.nft.NFT;
import com.example.restservice.nft.NFTRepository;
import com.example.restservice.nft.NftTransaction;
import com.example.restservice.nft.NftTransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@org.springframework.stereotype.Service
@Transactional
public class Service {

    @Autowired
    private NftTransactionRepository nftTransactionRepository;

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

    public User createUser(String email, String password, String firstname, String lastname, String nickname, NftUserType type)
            throws Exception {
        try {
            User newUser = new User(email, password, firstname, lastname, nickname, type);
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
        nftRepository.removeListing(nft.getId());
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

    public SessionToken createSessionToken(User user, String token) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 7); //minus number would decrement the days
        SessionToken sessionToken = new SessionToken(user, token, cal.getTime());
        sessionRepository.saveAndFlush(sessionToken);
        return sessionToken;
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

    public void deleteAllOffersByAuthorOnNft(User user,NFT nft) {
        offerRepository.deleteAllByAuthorOnNft(user.getID(), nft.getId());
    }

    public void createOffer(User user, Double price, NFT nft) {
        Offer offer = new Offer(user, price, nft);
        this.deleteAllOffersByAuthorOnNft(user, nft);
        offerRepository.saveAndFlush(offer);
    }

    public ArrayList<NFT> getAllListingsAsNFTs() {
        Collection<NFT> nftsListed = nftRepository.findAllListed();
        return new ArrayList<>(nftsListed);
    }

    public ArrayList<Offer> getOfferHistory(String nft_id) {
        return new ArrayList<Offer>(offerRepository.findAllNFTOffers(nft_id));
    }

    public Double getMinimumOfferPrice(String nft_id) {
        Double maxOfferPrice = (offerRepository.findMinimumOfferPrice(nft_id));
        return maxOfferPrice;
    }

    public void createNftTrancsaction(User buyer, User seller, NFT nft, CryptoType type, Date date, BigDecimal amount, BigDecimal postPurchaseBalance) {
        NftTransaction newNftTransaction  = new NftTransaction(buyer,seller,nft,type,date,amount,postPurchaseBalance);
        nftTransactionRepository.saveAndFlush(newNftTransaction);
    }
}
