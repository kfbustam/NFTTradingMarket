package com.example.restservice.nft;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Flight repository.
 */
public interface NFTRepository extends JpaRepository<NFT, String> {

}