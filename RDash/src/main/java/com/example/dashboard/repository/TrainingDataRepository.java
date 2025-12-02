package com.example.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dashboard.model.TrainingData;

@Repository
public interface TrainingDataRepository extends JpaRepository<TrainingData, Long>
{
}

