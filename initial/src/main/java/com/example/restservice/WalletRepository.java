package com.example.restservice;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.*;

/**
 * The interface Wallet repository.
 */
public interface WalletRepository extends JpaRepository<Wallet, String> {

  @Query(value="SELECT * FROM wallet w WHERE w.user_id=?1", nativeQuery = true)
  public Collection<Wallet> findUserWallets(String userID);
  
}