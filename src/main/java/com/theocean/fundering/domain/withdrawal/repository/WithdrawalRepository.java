package com.theocean.fundering.domain.withdrawal.repository;

import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
    @Query("SELECT w FROM Withdrawal w WHERE w.postId = :postId AND w.status = com.theocean.fundering.global.utils.ApprovalStatus.APPROVED ORDER BY w.withdrawalId DESC")
    Page<Withdrawal> getWithdrawalPage(@Param("postId") Long postId, Pageable pageable);

    @Query("SELECT w FROM Withdrawal w WHERE w.postId = :postId AND w.status = com.theocean.fundering.global.utils.ApprovalStatus.PENDING")
    List<Withdrawal> findWithdrawalByPostId(@Param("postId") Long postID);
}
