package com.example.restservice;

import java.util.Collection;

import org.springframework.data.jpa.repository.*;

import com.example.restservice.crypto.CryptoType;

/**
 * The interface Wallet repository.
 */
public interface WalletRepository extends JpaRepository<Wallet, String> {

  @Query(value="SELECT * FROM wallet w WHERE w.user_id=?1 AND w.type=?2", nativeQuery = true)
  public Collection<Wallet> findUserWalletByType(String userID, CryptoType type);

  @Query(value="SELECT * FROM wallet w WHERE w.user_id=?1", nativeQuery = true)
  public Collection<Wallet> findUserWallet(String userID);

}