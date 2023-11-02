package com.theocean.fundering.domain.withdrawal.repository;

import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
