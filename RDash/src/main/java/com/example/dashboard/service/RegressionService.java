package com.example.dashboard.service;

import com.example.dashboard.model.TrainingData;
import com.example.dashboard.model.PredictionResult;
import com.example.dashboard.model.PredictionRequest;
import com.example.dashboard.repository.TrainingDataRepository;
import com.example.dashboard.repository.PredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegressionService {

    private final TrainingDataRepository trainingRepo;
    private final PredictionRepository predictionRepo;

    @Autowired
    public RegressionService(TrainingDataRepository trainingRepo, PredictionRepository predictionRepo) {
        this.trainingRepo = trainingRepo;
        this.predictionRepo = predictionRepo;
    }

    // ================= Training =================
    public TrainingData saveTrainingData(TrainingData data) {
        return trainingRepo.save(data);
    }
 // Fetch all training data
    public List<TrainingData> getAllTrainingData() {
        return trainingRepo.findAll();
    }


    // ================= Prediction =================
    public PredictionResult predict(PredictionRequest request) {
        List<TrainingData> trainingDataList = trainingRepo.findAll();

        if (trainingDataList.isEmpty()) {
            throw new RuntimeException("No training data available! Train the model first.");
        }

        int n = trainingDataList.size();
        double[][] X = new double[n][3]; // [1, feature1, feature2]
        double[] Y = new double[n];

        for (int i = 0; i < n; i++) {
            TrainingData data = trainingDataList.get(i);
            X[i][0] = 1; // intercept
            X[i][1] = data.getFeature1();
            X[i][2] = data.getFeature2();
            Y[i] = data.getActualValue(); // updated field
        }

        // Compute coefficients using normal equation: B = (X^T * X)^-1 * X^T * Y
        double[][] Xt = transpose(X);
        double[][] XtX = multiply(Xt, X);
        double[][] XtX_inv = invert(XtX);
        double[][] XtY = multiply(Xt, Y);
        double[] B = multiplyMatrixVector(XtX_inv, XtY); // b0, b1, b2
// returns b0, b1, b2

        double predictedValue = B[0] + B[1] * request.getFeature1() + B[2] * request.getFeature2();

        // Save prediction
        PredictionResult result = new PredictionResult();
        result.setFeature1(request.getFeature1());
        result.setFeature2(request.getFeature2());
        result.setPredictedValue(predictedValue);
        predictionRepo.save(result);

        return result;
    }

    // ================= Get all predictions =================
    public List<PredictionResult> getAllPredictions() {
        return predictionRepo.findAll();
    }

    // ================= Helper Methods =================

    // Transpose matrix
    private double[][] transpose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] transposed = new double[cols][rows];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                transposed[j][i] = matrix[i][j];
        return transposed;
    }

    // Multiply two matrices
    private double[][] multiply(double[][] A, double[][] B) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int colsB = B[0].length;
        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++)
            for (int j = 0; j < colsB; j++)
                for (int k = 0; k < colsA; k++)
                    result[i][j] += A[i][k] * B[k][j];

        return result;
    }

    // Multiply matrix with vector to get double[]
   
 // Multiply matrix A (3x3) with column vector B (3x1) and return double[]
    private double[] multiplyMatrixVector(double[][] A, double[][] B) {
        int rows = A.length;
        int cols = A[0].length;
        double[] result = new double[rows];
        for (int i = 0; i < rows; i++) {
            result[i] = 0;
            for (int j = 0; j < cols; j++) {
                result[i] += A[i][j] * B[j][0];
            }
        }
        return result;
    }

    // Multiply matrix with vector Y (n x 1)
    private double[][] multiply(double[][] A, double[] Y) {
        int rows = A.length;
        int cols = A[0].length;
        double[][] result = new double[rows][1];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result[i][0] += A[i][j] * Y[j];
        return result;
    }

    // Invert 3x3 matrix
    private double[][] invert(double[][] m) {
        double det = m[0][0]*(m[1][1]*m[2][2]-m[2][1]*m[1][2]) -
                     m[0][1]*(m[1][0]*m[2][2]-m[2][0]*m[1][2]) +
                     m[0][2]*(m[1][0]*m[2][1]-m[2][0]*m[1][1]);

        if (det == 0) throw new RuntimeException("Matrix is singular, cannot invert");

        double invdet = 1.0 / det;
        double[][] minv = new double[3][3];

        minv[0][0] = (m[1][1]*m[2][2]-m[2][1]*m[1][2])*invdet;
        minv[0][1] = (m[0][2]*m[2][1]-m[0][1]*m[2][2])*invdet;
        minv[0][2] = (m[0][1]*m[1][2]-m[0][2]*m[1][1])*invdet;
        minv[1][0] = (m[1][2]*m[2][0]-m[1][0]*m[2][2])*invdet;
        minv[1][1] = (m[0][0]*m[2][2]-m[0][2]*m[2][0])*invdet;
        minv[1][2] = (m[1][0]*m[0][2]-m[0][0]*m[1][2])*invdet;
        minv[2][0] = (m[1][0]*m[2][1]-m[2][0]*m[1][1])*invdet;
        minv[2][1] = (m[2][0]*m[0][1]-m[0][0]*m[2][1])*invdet;
        minv[2][2] = (m[0][0]*m[1][1]-m[1][0]*m[0][1])*invdet;

        return minv;
    }
}
