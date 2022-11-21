package com.example.restservice;

import org.springframework.data.jpa.repository.*;

/**
 * The interface Wallet repository.
 */
public interface WalletRepository extends JpaRepository<Wallet, String> {

}