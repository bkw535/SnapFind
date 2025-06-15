package com.iosProject.backend_api.search.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.List;

@Service
public class VisionService {

    private final ImageAnnotatorClient visionClient;

    public VisionService(@Value("${google.vision.credentials.path}") Resource credentialsResource) throws Exception {

        ServiceAccountCredentials credentials = ServiceAccountCredentials
                .fromStream(credentialsResource.getInputStream());

        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        this.visionClient = ImageAnnotatorClient.create(settings);
    }

    public List<String> detectLabels(byte[] imageBytes) throws Exception {
        ByteString imgBytes = ByteString.copyFrom(imageBytes);

        Image image = Image.newBuilder().setContent(imgBytes).build();
        Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();

        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feature)
                .setImage(image)
                .build();

        AnnotateImageResponse response = visionClient.batchAnnotateImages(List.of(request)).getResponses(0);

        return response.getLabelAnnotationsList().stream()
                .map(annotation -> annotation.getDescription() + " (" + annotation.getScore() + ")")
                .toList();
    }
}
