package com.example.cryptography.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ledger")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "node_number", nullable = false)
    private String nodeNumber;
    
    @Column(name = "key_value", nullable = false)
    private String keyValue;
    
    @Column(name = "transferred_node", nullable = false)
    private String transferredNode;
    
    @Column(name = "transaction_data", columnDefinition = "TEXT")
    private String transactionData;
    
    @Column(name = "encrypted_text", columnDefinition = "TEXT")
    private String encryptedText;
    
    @Column(name = "cipher_type")
    private String cipherType;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "background_color")
    private String backgroundColor;
    
    @Column(name = "text_color")
    private String textColor;
    
    // Default constructor (required by JPA)
    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }
    
    // Parameterized constructor
    public Transaction(String nodeNumber, String keyValue, String transferredNode, 
                      String transactionData, String encryptedText, String cipherType) {
        this.nodeNumber = nodeNumber;
        this.keyValue = keyValue;
        this.transferredNode = transferredNode;
        this.transactionData = transactionData;
        this.encryptedText = encryptedText;
        this.cipherType = cipherType;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNodeNumber() { return nodeNumber; }
    public void setNodeNumber(String nodeNumber) { this.nodeNumber = nodeNumber; }
    
    public String getKeyValue() { return keyValue; }
    public void setKeyValue(String keyValue) { this.keyValue = keyValue; }
    
    public String getTransferredNode() { return transferredNode; }
    public void setTransferredNode(String transferredNode) { this.transferredNode = transferredNode; }
    
    public String getTransactionData() { return transactionData; }
    public void setTransactionData(String transactionData) { this.transactionData = transactionData; }
    
    public String getEncryptedText() { return encryptedText; }
    public void setEncryptedText(String encryptedText) { this.encryptedText = encryptedText; }
    
    public String getCipherType() { return cipherType; }
    public void setCipherType(String cipherType) { this.cipherType = cipherType; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
    
    public String getTextColor() { return textColor; }
    public void setTextColor(String textColor) { this.textColor = textColor; }
}
