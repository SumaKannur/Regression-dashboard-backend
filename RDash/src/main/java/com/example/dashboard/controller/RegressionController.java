package com.example.dashboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dashboard.model.PredictionRequest;
import com.example.dashboard.model.PredictionResult;
import com.example.dashboard.model.TrainingData;
import com.example.dashboard.service.RegressionService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class RegressionController {

    private final RegressionService regressionService;

    @Autowired
    public RegressionController(RegressionService regressionService) {
        this.regressionService = regressionService;
    }

    @PostMapping("/train")
    public TrainingData trainModel(@RequestBody TrainingData trainingData) {
        return regressionService.saveTrainingData(trainingData);
    }

    @GetMapping("/train")
    public List<TrainingData> getTrainingData() {
        return regressionService.getAllTrainingData();
    }

    @PostMapping("/predict")
    public PredictionResult predict(@RequestBody PredictionRequest request) {
        return regressionService.predict(request);
    }

    @GetMapping("/predictions")
    public List<PredictionResult> getAllPredictions() {
        return regressionService.getAllPredictions();
    }
}
