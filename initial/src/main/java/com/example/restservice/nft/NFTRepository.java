package com.example.restservice.nft;

import java.util.Collection;
import java.util.List;

import com.example.restservice.nft.NFT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Flight repository.
 */
public interface NFTRepository extends JpaRepository<NFT, String> {
  @Query(value="SELECT * FROM nft c WHERE c.wallet_id=?1", nativeQuery = true)
  public Collection<NFT> findByWalletID(String wallet_id);

}