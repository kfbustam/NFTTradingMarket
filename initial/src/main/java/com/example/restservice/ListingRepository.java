package com.example.restservice;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Flight repository.
 */
public interface ListingRepository extends JpaRepository<Listing, String> {
    @Query(value="SELECT * FROM listing l WHERE l.nft_id=?1 and l.status='AVAILABLE'", nativeQuery = true)
    public Collection<Listing> findAllNFTListings(String nft_id);

    @Modifying
    @Query(value="UPDATE listing n SET n.status='SOLD' WHERE nft_id=?1", nativeQuery = true)
    public void updateSaleStatus(String nft_id);
}