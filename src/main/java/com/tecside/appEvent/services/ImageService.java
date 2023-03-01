package com.tecside.appEvent.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class ImageService {


    @Autowired
    @Value("{$app.storage.account.name}")
    private String accountName;

    @Value("{$app.storage.account.key}")
    private String key;

    @Value("{$app.storage.account.endpoint.sufix}")
    private String endpointSufix;

    @Value("{$app.storage.account.container}")
    private String container;

    @Value("{$app.storage.account.EndpointProtocol}")
    private String endpointProtocol;

    @Value("{$app.storage.account.pathName}")
    private String pathName;

    public ImageService() {

    }

    public String blobUpload(MultipartFile file) throws Exception{

        String conectionString =  "DefaultEndpointsProtocol="+this.endpointProtocol+";"
                + "AccountName="+accountName+";"
                + "AccountKey="+key+";"
                + "EndpointSuffix="+endpointSufix+"";

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(conectionString).buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(this.container);

        BlobClient blobClient = containerClient.getBlobClient(file.getName());

        File imageFile = new File(pathName);

        blobClient.uploadFromFile(imageFile.getAbsolutePath(), true);

        return file.getName();


    }

}
