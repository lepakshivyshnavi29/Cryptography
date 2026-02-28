
package com.example.cryptography.service;

import com.example.cryptography.entity.Transaction;
import com.example.cryptography.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private EncryptionService encryptionService;
    
    @Autowired
    private QuantumCryptographyService quantumService;
    
    public Transaction createTransaction(String sourceNodeId, String destinationNodeId, 
                                String originalText, String key) {
        
        // Encrypt the text
        EncryptionService.EncryptionResult encryptionResult =encryptionService.encryptText(originalText, key);
        
        // Generate quantum colors for optical visualization
        QuantumCryptographyService.QuantumColors colors = quantumService.generateQuantumColors();
        
        // Create transaction
        Transaction transaction = new Transaction(sourceNodeId, key,  destinationNodeId, originalText, encryptionResult.getEncryptedText(), encryptionResult.getCipherType());
        
        transaction.setBackgroundColor(colors.getBackgroundColor());
        transaction.setTextColor(colors.getTextColor());
        
        return transactionRepository.save(transaction);
    }
    
    public List<Transaction> getTransactionsForNode(String nodeId) {
        return transactionRepository.findByNodeNumberOrderByTimestampDesc(nodeId);
    }
    
    public List<Transaction> getReceivedTransactionsForNode(String nodeId) {
        return transactionRepository.findByTransferredNodeOrderByTimestampDesc(nodeId);
    }
    
    public List<Transaction> getAllTransactionsForNode(String nodeId) {
        return transactionRepository.findByNodeNumberOrTransferredNodeOrderByTimestampDesc(nodeId, nodeId);
    }
    
    public String decryptTransaction(Transaction transaction, String key) {
        return encryptionService.decryptText(
            transaction.getEncryptedText(), 
            key, 
            transaction.getCipherType()
        );
    }
}