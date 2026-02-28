
package com.example.cryptography.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class EncryptionService {
    
    private final Random random = new Random();
    
    public EncryptionResult encryptText(String text, String key) {
        int cipherChoice = random.nextInt(3); // 0, 1, or 2
        String cipherType;
        String encryptedText;
        
        switch (cipherChoice) {
            case 0:
                cipherType = "AFFINE";
                encryptedText = affineEncrypt(text, key);
                break;
            case 1:
                cipherType = "CAESAR";
                encryptedText = caesarEncrypt(text, key);
                break;
            case 2:
                cipherType = "BLOCK";
                encryptedText = blockEncrypt(text, key);
                break;
            default:
                cipherType = "CAESAR";
                encryptedText = caesarEncrypt(text, key);
        }
        
        return new EncryptionResult(encryptedText, cipherType);
    }
    
    public String decryptText(String encryptedText, String key, String cipherType) {
        switch (cipherType.toUpperCase()) {
            case "AFFINE":
                return affineDecrypt(encryptedText, key);
            case "CAESAR":
                return caesarDecrypt(encryptedText, key);
            case "BLOCK":
                return blockDecrypt(encryptedText, key);
            default:
                return "Decryption failed: Unknown cipher type";
        }
    }
    
    private String affineEncrypt(String text, String key) {
        int a = Math.abs(key.hashCode()) % 25 + 1;
        int b = Math.abs(key.hashCode()) % 26;
        
        StringBuilder encrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int base = Character.isUpperCase(c) ? 'A' : 'a';
                int pos = c - base;
                int newPos = (a * pos + b) % 26;
                encrypted.append((char)(base + newPos));
            } else {
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }
    
    private String affineDecrypt(String encryptedText, String key) {
        int a = Math.abs(key.hashCode()) % 25 + 1;
        int b = Math.abs(key.hashCode()) % 26;
        int aInverse = modInverse(a, 26);
        
        StringBuilder decrypted = new StringBuilder();
        for (char c : encryptedText.toCharArray()) {
            if (Character.isLetter(c)) {
                int base = Character.isUpperCase(c) ? 'A' : 'a';
                int pos = c - base;
                int newPos = (aInverse * (pos - b + 26)) % 26;
                decrypted.append((char)(base + newPos));
            } else {
                decrypted.append(c);
            }
        }
        return decrypted.toString();
    }
    
    private String caesarEncrypt(String text, String key) {
        int shift = Math.abs(key.hashCode()) % 26;
        StringBuilder encrypted = new StringBuilder();
        
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int base = Character.isUpperCase(c) ? 'A' : 'a';
                int pos = c - base;
                int newPos = (pos + shift) % 26;
                encrypted.append((char)(base + newPos));
            } else {
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }
    
    private String caesarDecrypt(String encryptedText, String key) {
        int shift = Math.abs(key.hashCode()) % 26;
        StringBuilder decrypted = new StringBuilder();
        
        for (char c : encryptedText.toCharArray()) {
            if (Character.isLetter(c)) {
                int base = Character.isUpperCase(c) ? 'A' : 'a';
                int pos = c - base;
                int newPos = (pos - shift + 26) % 26;
                decrypted.append((char)(base + newPos));
            } else {
                decrypted.append(c);
            }
        }
        return decrypted.toString();
    }
    
    private String blockEncrypt(String text, String key) {
        int blockSize = (Math.abs(key.hashCode()) % 5) + 2; // Block size between 2-6
        StringBuilder encrypted = new StringBuilder();
        
        for (int i = 0; i < text.length(); i += blockSize) {
            String block = text.substring(i, Math.min(i + blockSize, text.length()));
            encrypted.append(new StringBuilder(block).reverse().toString());
        }
        
        return caesarEncrypt(encrypted.toString(), key); // Additional Caesar encryption
    }
    
    private String blockDecrypt(String encryptedText, String key) {
        String caesarDecrypted = caesarDecrypt(encryptedText, key);
        int blockSize = (Math.abs(key.hashCode()) % 5) + 2;
        StringBuilder decrypted = new StringBuilder();
        
        for (int i = 0; i < caesarDecrypted.length(); i += blockSize) {
            String block = caesarDecrypted.substring(i, Math.min(i + blockSize, caesarDecrypted.length()));
            decrypted.append(new StringBuilder(block).reverse().toString());
        }
        
        return decrypted.toString();
    }
    
    private int modInverse(int a, int m) {
        for (int i = 1; i < m; i++) {
            if ((a * i) % m == 1) {
                return i;
            }
        }
        return 1;
    }
    
    public static class EncryptionResult {
        private final String encryptedText;
        private final String cipherType;
        
        public EncryptionResult(String encryptedText, String cipherType) {
            this.encryptedText = encryptedText;
            this.cipherType = cipherType;
        }
        
        public String getEncryptedText() { return encryptedText; }
        public String getCipherType() { return cipherType; }
    }
}