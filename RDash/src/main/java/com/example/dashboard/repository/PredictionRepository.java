package com.example.dashboard.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dashboard.model.PredictionResult;

@Repository
public interface PredictionRepository extends JpaRepository<PredictionResult, Long> {}
