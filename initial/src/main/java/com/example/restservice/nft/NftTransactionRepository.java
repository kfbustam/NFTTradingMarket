package com.example.restservice.nft;

import com.example.restservice.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

/**
 * The interface Flight repository.
 */
public interface NftTransactionRepository extends JpaRepository<NftTransaction, String> {
    @Query(value="SELECT * FROM transactions w WHERE w.buyer_id=?1", nativeQuery = true)
    public Collection<NftTransaction> findBuyerTransactions(String userID);

    @Query(value="SELECT * FROM transactions w WHERE w.seller_id=?1", nativeQuery = true)
    public Collection<NftTransaction> findSellerTransactions(String userID);

}