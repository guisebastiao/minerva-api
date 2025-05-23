package com.minerva.minervaapi.repositories;

import com.minerva.minervaapi.models.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, UUID> {
    Optional<ResetPassword> findByToken(UUID token);

    @Modifying
    @Query("DELETE FROM ResetPassword rp WHERE rp.user.id = :userId")
    void deleteByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM ResetPassword rp WHERE rp.expiryDate < :date")
    void deleteAllByExpiryDateBefore(@Param("date") LocalDateTime date);
}
