package com.example.restservice.nft;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Flight repository.
 */
public interface NFTRepository extends JpaRepository<NFT, String> {
  @Query(value="SELECT * FROM nft c WHERE c.wallet_id=?1", nativeQuery = true)
  public Collection<NFT> findByWalletID(String wallet_id);

  @Query(value="SELECT SUM(price) FROM nft n WHERE n.wallet_id=?1", nativeQuery = true)
  public double findTotalPriceInWallet(String wallet_id);

  @Query(value="SELECT * FROM nft n JOIN listing l on l.id=n.listing_id WHERE n.listing_id IS NOT NULL AND l.status = 'AVAILABLE'", nativeQuery = true)
  public Collection<NFT> findAllListed();

  @Modifying
  @Query(value="UPDATE nft n SET n.listing_id=NULL WHERE id=?1", nativeQuery = true)
  public void removeListing(String nft_id);
}