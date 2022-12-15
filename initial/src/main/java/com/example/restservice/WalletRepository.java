package com.example.restservice;

import com.example.restservice.crypto.CryptoType;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

/**
 * The interface Wallet repository.
 */
public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<User> findByUserAndType(User user, CryptoType cryptoType);
}