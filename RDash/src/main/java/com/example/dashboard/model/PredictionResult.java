package com.example.dashboard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity
public class PredictionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double feature1;
    private double feature2;
    private double predictedValue;
    public PredictionResult() {}
    public PredictionResult(double feature1, double feature2, double predictedValue) {
        this.feature1 = feature1;
        this.feature2 = feature2;
        this.predictedValue = predictedValue;
    }
    

    // Getters and setters
}
