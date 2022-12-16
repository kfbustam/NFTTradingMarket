package com.example.restservice;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Flight repository.
 */
public interface OfferRepository extends JpaRepository<Offer, String> {
  @Query(value="SELECT * FROM offer o WHERE o.nft_id=?1", nativeQuery = true)
  public Collection<Offer> findAllNFTOffers(String nft_id);

  @Query(value="SELECT MAX(o.offer_price) FROM offer o WHERE o.nft_id=?1", nativeQuery = true)
  public Double findMinimumOfferPrice(String nft_id);

  @Modifying
  @Query(value="DELETE FROM offer o WHERE o.user_id=?1 AND o.nft_id=?2", nativeQuery = true)
  public void deleteAllByAuthorOnNft(String user_id, String nft_id);
}