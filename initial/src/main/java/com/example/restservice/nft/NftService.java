package com.example.restservice.nft;

import com.example.restservice.*;
import com.example.restservice.crypto.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
@Transactional
public class NftService {

    @Autowired
    private NFTRepository nftRepository;

    @Autowired
    private WalletRepository walletRepository;

    public NFT createNft (Path fileNameAndPath, CryptoType type, String walletId, String name, String description, double price) {
        //todo is any duplicate check required?

        NFT nft = new NFT();
        nft.setWallet(walletRepository.findById(walletId).orElseThrow());
        nft.setImageUrl(fileNameAndPath.toString());
        nft.setNftType(type);
        nft.setTokenId(UUID.randomUUID().toString());
        nft.setSmartContractAddress(UUID.randomUUID().toString());
        nft.setName(name);
        nft.setPrice(price);
        nft.setDescription(description);
        nft.setLastRecordedTime(new Date());
        // todo add asset url

        nftRepository.save(nft);

        return nft;
    }

    public List<NFT> getAllNfts() {
        return nftRepository.findAll();
    }

    public Optional<NFT> getNFT(String id) {
        return nftRepository.findById(id);
    }
}
