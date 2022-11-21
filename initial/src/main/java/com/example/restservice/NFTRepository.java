package com.example.restservice;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.restservice.NFT;

/**
 * The interface Flight repository.
 */
public interface NFTRepository extends JpaRepository<NFT, String> {

}