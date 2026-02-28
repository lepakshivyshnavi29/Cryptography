
package com.example.cryptography.repository;

import com.example.cryptography.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByNodeNumberOrderByTimestampDesc(String nodeNumber);
    List<Transaction> findByTransferredNodeOrderByTimestampDesc(String transferredNode);
    List<Transaction> findByNodeNumberOrTransferredNodeOrderByTimestampDesc(String nodeNumber, String transferredNode);
}