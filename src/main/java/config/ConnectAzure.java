package config;


import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

public class ConnectAzure {
    private static final String CONNECTION_STRING = System.getenv("CONNECTION_STRONG");
    private static final String CONTAINER_NAME = System.getenv("CONTAINER_NAME");


    public static BlobContainerClient getContainerClient() {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(CONNECTION_STRING)
                .buildClient();

        return blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
    }
}
