package org.id.bankspringbatch.repository;

import org.id.bankspringbatch.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransactionRepository extends JpaRepository<BankTransaction,Long> {
}
