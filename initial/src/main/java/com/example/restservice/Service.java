package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.example.restservice.nft.NftType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@org.springframework.stereotype.Service
@Transactional
public class Service {

    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;

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

    public User createUser(String email, String password, String firstname, String lastname, String nickname) {
        User newUser = new User(email, password, firstname, lastname, nickname);
        User userResponse = userRepository.saveAndFlush(newUser);
        return userResponse;
    }

    public Wallet createWallet(User user, NftType type) {
        Wallet newWallet = new Wallet(user, type);
        Wallet walletCreated = walletRepository.saveAndFlush(newWallet);
        return walletCreated;
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

    public ArrayList<Wallet> getUserWallets(User user) {
        Collection<Wallet> usersWallets = walletRepository.findUserWallets(user.getID());
        return new ArrayList<>(usersWallets);
    }

    public List<CryptographicAsset> getWalletContents(Wallet wallet) {
        Collection<NFT> nfts = nftRepository.findByWalletID(wallet.getID());
        Collection<CryptoCurrency> cryptos = cryptoCurrencyRepository.findByWalletID(wallet.getID());
        List<CryptographicAsset> walletContents = Stream.concat(nfts.stream(), cryptos.stream()).collect(Collectors.toList());
        return walletContents;
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
}
