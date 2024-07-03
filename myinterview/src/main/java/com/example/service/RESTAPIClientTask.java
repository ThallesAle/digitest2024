package com.example.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * This task expects you to create an implementation of a Rest API client.
 * Your code should call the API endpoint related below.
 * After receiving the JSON Response, print out how many records exists for each gender
 * API endpoint => https://3ospphrepc.execute-api.us-west-2.amazonaws.com/prod/RDSLambda 
 * 
 * >>> Bonus <<<
 * Generate a CSV file containing many records exists for each gender and save this file to AWS S3 Bucket
 * The filename need to contains your entire name, separated by uderscore. Example: john_lennon.csv
 * AWS S3 bucket name => interview-digiage
 * The credentials you can find in Coodesh platform or ask via e-mail for recrutamento@digiage.com.br
 */

public class RESTAPIClientTask {

    private static final String API_URL = "https://3ospphrepc.execute-api.us-west-2.amazonaws.com/prod/RDSLambda";
    private static final String REGION = "us-west-2";
    private static final String SERVICE = "execute-api";
    private static final String S3_BUCKET_NAME = "interview-digiage";

    public static void main(String[] args) throws IOException, InterruptedException, JSONException {
        Map<String, Integer> genderCount = getGenderCountFromAPI();

        String csvData = generateCSVData(genderCount);
        String filename = "export_file.csv";
        saveCsvToS3(filename, csvData);

        System.out.println("Contagem por gênero salva com sucesso no arquivo " + filename + " no bucket S3.");
    }

    private static Map<String, Integer> getGenderCountFromAPI() throws IOException, InterruptedException, JSONException {
        //metodo para consumir api e retornar a map.
        return null;
    }

    private static String generateCSVData(Map<String, Integer> genderCount) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Genero,Contagem\n");
        for (Map.Entry<String, Integer> entry : genderCount.entrySet()) {
            csvBuilder.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
        }
        return csvBuilder.toString();
    }

    private static void saveCsvToS3(String filename, String csvData) throws IOException {

        AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .withCredentials(credentialsProvider)
                .build();

        File tempFile = File.createTempFile(filename, null);
        FileWriter writer = new FileWriter(tempFile);
        writer.write(csvData);
        writer.close();

        PutObjectRequest request = new PutObjectRequest(S3_BUCKET_NAME, filename, tempFile);
        s3Client.putObject(request);

        tempFile.delete();
    }
}