package com.app.tradeguess.repository;

import com.app.tradeguess.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByTelegramId(Long telegramId);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameIgnoreCase(String username);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<User> findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(
            @Param("query") String query);

    boolean existsByUsername(String username);

    List<User> findByRole(String role);

    List<User> findByIsBlocked(Boolean isBlocked);

    @Query("SELECT u FROM User u WHERE " +
            "u.username = :username OR " +
            "u.username = CONCAT('@', :username)")
    Optional<User> findByUsernameWithAt(@Param("username") String username);}
