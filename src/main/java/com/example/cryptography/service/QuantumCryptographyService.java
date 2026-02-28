package com.example.cryptography.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class QuantumCryptographyService {
    
    private final Random random = new Random();
    
    // Polarizer-visible color combinations
    private final String[][] quantumColors = {
        {"#FF0000", "#00FF00"}, // Red background, Green text
        {"#0000FF", "#FFFF00"}, // Blue background, Yellow text
        {"#FF00FF", "#00FFFF"}, // Magenta background, Cyan text
        {"#800000", "#00FF80"}, // Dark red background, Light green text
        {"#000080", "#FF8000"}, // Dark blue background, Orange text
        {"#008080", "#FF0080"}, // Teal background, Pink text
        {"#400040", "#80FF80"}, // Dark purple background, Light green text
        {"#004000", "#FF4040"}  // Dark green background, Light red text
    };
    
    public QuantumColors generateQuantumColors() {
        int colorIndex = random.nextInt(quantumColors.length);
        String backgroundColor = quantumColors[colorIndex][0];
        String textColor = quantumColors[colorIndex][1];
        
        return new QuantumColors(backgroundColor, textColor);
    }
    
    public static class QuantumColors {
        private final String backgroundColor;
        private final String textColor;
        
        public QuantumColors(String backgroundColor, String textColor) {
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
        }
        
        public String getBackgroundColor() { return backgroundColor; }
        public String getTextColor() { return textColor; }
    }
}