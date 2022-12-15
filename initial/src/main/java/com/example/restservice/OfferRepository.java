package com.example.restservice;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Flight repository.
 */
public interface OfferRepository extends JpaRepository<Offer, String> {
  @Query(value="SELECT * FROM offer o WHERE o.nft_id=?1", nativeQuery = true)
  public Collection<Offer> findAllNFTOffers(String nft_id);
}