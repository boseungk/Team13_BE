package com.theocean.fundering.domain.payment.repository;


import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p.memberId FROM Payment p WHERE p.postId = :postId ORDER BY p.amount DESC")
    List<Long> findAllSupporterByPostId(@Param("postId") Long postId);

}
