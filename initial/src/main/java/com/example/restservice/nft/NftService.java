package com.example.restservice.nft;

import com.example.restservice.*;
import com.example.restservice.crypto.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@org.springframework.stereotype.Service
@Transactional
public class NftService {

    @Autowired
    private NFTRepository nftRepository;

    @Autowired
    private NftTransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private S3Repository s3Repository;

    public InputStream getNftImage(String fileName) throws IOException {
        return s3Repository.getImage(fileName);
    }
    public NFT createNft (MultipartFile file, CryptoType type, String walletId, String name, String description, NftCategory category) throws IOException {
        //todo is any duplicate check required?

        String imageUrl = s3Repository.uploadFile(file);

        NFT nft = new NFT();
        nft.setWallet(walletRepository.findById(walletId).orElseThrow());
        nft.setImageUrl(imageUrl);
        nft.setNftType(type);
        nft.setTokenId(UUID.randomUUID().toString());
        nft.setSmartContractAddress(UUID.randomUUID().toString());
        nft.setName(name);
        //nft.setPrice(price);
        nft.setCategory(category);
        nft.setDescription(description);
        nft.setLastRecordedTime(new Date());
        // todo add asset url

        nftRepository.save(nft);

        return nft;
    }

    public List<NFT> getAllNfts() {
        return nftRepository.findAll();
    }

    public List<NFT> getAllNftsByUser(User user) {
        List<NFT> nftList = new ArrayList<>();
        List<Wallet> wallets = walletRepository.findUserWallet(user.getID())
                .stream().collect(Collectors.toList());

        for(Wallet wallet : wallets) {
            List<NFT> nftsInWallet = nftRepository.findByWalletID(wallet.getId())
                    .stream().collect(Collectors.toList());
            nftList.addAll(nftsInWallet);
        }

        return nftList;
    }

    public Optional<NFT> getNFT(String id) {
        return nftRepository.findById(id);
    }

    public Collection<NftTransaction> getTransactions(User user) {
        var buyerStream = transactionRepository.findBuyerTransactions(user.getID());
        var sellerStream = transactionRepository.findSellerTransactions(user.getID());

        buyerStream.addAll(sellerStream);

        return buyerStream.stream()
                .sorted(Comparator.comparing(NftTransaction::getDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }
}
