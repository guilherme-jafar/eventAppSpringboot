package com.tecside.appEvent.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.tecside.appEvent.errors.ErrorMessages;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ImageService {


    //@Autowired
    @Value("${app.storage.account.name}")
    private String accountName;

    @Value("${app.storage.account.key}")
    private String key;

    @Value("${app.storage.account.endpointSufix}")
    private String endpointSufix;

    @Value("${app.storage.account.container}")
    private String container;

    @Value("${app.storage.account.EndpointProtocol}")
    private String endpointProtocol;

    @Value("${app.storage.account.pathName}")
    private String pathName;

    @Value("${app.storage.allowedImageExtensions}")
    private String[] allowedExtensions;

    public ImageService() {

    }

    public String blobUpload(MultipartFile file, String name) throws IOException {


        String imageName = createImageName(name, file);

        BlobClient blobClient = createBlobClient(imageName);


        blobClient.upload(file.getInputStream(), file.getSize(), false);

        return imageName;


    }

    public void blobDelete(String imageName) {


        BlobClient blobClient = createBlobClient(imageName);

        blobClient.deleteIfExists();


    }

    public BlobClient createBlobClient(String imageName) {
        String conectionString = "DefaultEndpointsProtocol=" + this.endpointProtocol + ";"
                + "AccountName=" + this.accountName + ";"
                + "AccountKey=" + this.key + ";"
                + "EndpointSuffix=" + this.endpointSufix;


        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(conectionString)
                .buildClient();


        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(this.container);

        return containerClient.getBlobClient(imageName);
    }


    public String createImageName(String objectName, MultipartFile file) throws FileUploadException {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        if (!isExtensionValid(file.getContentType())) {
            throw new FileUploadException(ErrorMessages.WRONG_IMAGE_EXTENSION);
        }

        String creationTime = String.valueOf(System.currentTimeMillis());
        return objectName +
                "-" +
                creationTime +
                "." +
                extension;


    }

    public boolean isExtensionValid(String extension) {
        List<String> stringList = Arrays.asList(this.allowedExtensions);
        return stringList.contains(extension);
    }


}
