package com.example.AnimalShelter.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;

import java.io.ByteArrayInputStream;

public class AzureBlobService {
    private final BlobContainerClient containerClient;

    public AzureBlobService(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    public String uploadImage(byte[] imageBytes, String fileName) {
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(new ByteArrayInputStream(imageBytes), imageBytes.length, true);
        return blobClient.getBlobUrl();
    }

    public byte[] downloadImage(String fileName) {
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        return blobClient.downloadContent().toBytes();
    }

}
