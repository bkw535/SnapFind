package com.iosProject.backend_api.search.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class VisionService {

    private final ImageAnnotatorClient visionClient;

    public VisionService() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new FileInputStream("/app/credentials.json")
        );

        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        this.visionClient = ImageAnnotatorClient.create(settings);
    }

    public VisionService(@Value("${google.vision.credentials.path}") Resource credentialsResource) throws Exception {
        ServiceAccountCredentials credentials = ServiceAccountCredentials
                .fromStream(credentialsResource.getInputStream());

        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        this.visionClient = ImageAnnotatorClient.create(settings);
    }

    public List<String> detectLabels(byte[] imageBytes) {
        List<String> labels = new ArrayList<>();

        ByteString byteString = ByteString.copyFrom(imageBytes);

        Image image = Image.newBuilder().setContent(byteString).build();
        Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();

        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feature)
                .setImage(image)
                .build();

        AnnotateImageResponse response = visionClient.batchAnnotateImages(Collections.singletonList(request)).getResponses(0);

        if (response.hasError()) {
            log.error("Vision API error: {}", response.getError().getMessage());
            return labels;
        }

        response.getLabelAnnotationsList().forEach(annotation -> {
            labels.add(annotation.getDescription());
            log.info("Label: {} (score: {})", annotation.getDescription(), annotation.getScore());
        });

        return labels;
    }

    public List<String> extractLabels(MultipartFile file) {
        try {
            return detectLabels(file.getBytes());
        } catch (IOException e) {
            log.error("Failed to extract bytes from MultipartFile", e);
            return List.of();
        }
    }
}