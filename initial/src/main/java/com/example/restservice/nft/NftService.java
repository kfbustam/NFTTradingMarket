package com.example.restservice.nft;

import com.example.restservice.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class NftService {

    @Autowired
    private NFTRepository nftRepository;

    @Autowired
    private WalletRepository walletRepository;


}
