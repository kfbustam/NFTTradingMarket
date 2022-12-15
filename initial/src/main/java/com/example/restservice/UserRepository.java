package com.example.restservice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface User repository.
 */
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * Find by email optional.
     *
     * @param String the email
     * @return the optional
     */
    Optional<User> findByEmail(String email);
}