package com.banking.banking.repository;

import com.banking.banking.model.FundTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {
}