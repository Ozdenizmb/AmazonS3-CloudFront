package com.amazon.asset.service.impl;

import com.amazon.asset.exception.ErrorMessages;
import com.amazon.asset.exception.ImageException;
import com.amazon.asset.model.ImagesEntity;
import com.amazon.asset.repository.ImageRepository;
import com.amazon.asset.service.ImageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository repository;
    private final AmazonS3 amazonS3Client;

    @Value("${file.allowed-formats}")
    private String[] allowedFormats;
    @Value("${file.default-height}")
    private int defaultImageHeight;
    @Value("${file.default-width}")
    private int defaultImageWidth;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Value("${aws.cdn.path}")
    private String cdnPath;

    @Override
    public String uploadImage(MultipartFile file) {
        String randomName = UUID.randomUUID().toString().replace("-", "");
        String fileType = Objects.requireNonNull(file.getContentType()).split("/")[1];

        if(!Arrays.asList(allowedFormats).contains(fileType)) {
            throw ImageException.withStatusAndMessage(HttpStatus.BAD_REQUEST, ErrorMessages.UNSUPPORTED_FILE_TYPE);
        }

        String fileName = randomName + "." + fileType;

        try {

            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            BufferedImage resizedImage = new BufferedImage(defaultImageWidth, defaultImageHeight, originalImage.getType());

            Graphics2D writer = resizedImage.createGraphics();
            writer.drawImage(originalImage, 0, 0, defaultImageWidth, defaultImageHeight, null);
            writer.dispose();

            File tempFile = File.createTempFile(randomName, "." + fileType);
            ImageIO.write(resizedImage, fileType, tempFile);

            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, tempFile));

            tempFile.delete();

        } catch (IOException e) {
            throw ImageException.withStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.IMAGE_CANNOT_WRITE);
        }

        ImagesEntity image = ImagesEntity.builder()
                .name(fileName)
                .type(file.getContentType())
                .filePath("AMAZON S3")
                .build();

        repository.save(image);

        return fileName;
    }

    @Override
    public byte[] getImage(String fileName) {

        Optional<ImagesEntity> response = repository.findByName(fileName);

        if(response.isPresent()) {

            try {
                String cdnUrl = cdnPath + fileName;
                URL url = new URI(cdnUrl).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();

                return IOUtils.toByteArray(inputStream);
            } catch (IOException | URISyntaxException e) {
                throw ImageException.withStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.IMAGES_NOT_FOUND);
            }

        }
        else {
            throw ImageException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.IMAGES_NOT_FOUND);
        }

    }

    @Override
    public Boolean deleteImage(String fileName) {

        Optional<ImagesEntity> response = repository.findByName(fileName);

        if(response.isPresent()){
            ImagesEntity existImage = response.get();
            repository.delete(existImage);

            try {
                amazonS3Client.deleteObject(bucketName, fileName);
            } catch (Exception e) {
                throw ImageException.withStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.IMAGE_CANNOT_DELETE);
            }

            return true;
        }
        else {
            throw ImageException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.IMAGES_NOT_FOUND);
        }

    }
}
