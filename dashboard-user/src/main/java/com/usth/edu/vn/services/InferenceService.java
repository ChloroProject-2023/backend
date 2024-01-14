package com.usth.edu.vn.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usth.edu.vn.exception.CustomException;

import jakarta.enterprise.context.RequestScoped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RequestScoped
public class InferenceService {

  public Training resultTrainEval(int pca_dim) throws IOException, CustomException {
    String connectURL = "http://0.0.0.0:5000/model/MPAP/Linear_Regression/run?ndim=" + pca_dim + "&user=MPAP";
    URL url = new URL(connectURL);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      StringBuilder sb = new StringBuilder();
      Scanner scanner = new Scanner(connection.getInputStream());
      while (scanner.hasNext()) {
        sb.append(scanner.nextLine());
      }
      ObjectMapper objectMapper = new ObjectMapper();
      Training training = objectMapper.readValue(String.valueOf(sb), Training.class);
      scanner.close();
      return training;
    } else {
      throw new CustomException("That seems not working at all!");
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  private static class Training {
    private String message;
    private String result;
  }
}
