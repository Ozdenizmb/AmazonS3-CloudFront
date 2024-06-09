package com.amazon.asset.service.impl;

import com.amazon.asset.exception.ErrorMessages;
import com.amazon.asset.exception.FileException;
import com.amazon.asset.model.FileEntity;
import com.amazon.asset.repository.FileRepository;
import com.amazon.asset.service.FileService;
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
public class FileServiceImpl implements FileService {

    private final FileRepository repository;
    private final AmazonS3 amazonS3Client;

    @Value("${file.allowed-formats}")
    private String[] allowedFormats;
    @Value("${file.default-image-height}")
    private int defaultImageHeight;
    @Value("${file.default-image-width}")
    private int defaultImageWidth;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Value("${aws.cdn.path}")
    private String cdnPath;

    @Override
    public String uploadFile(MultipartFile file) {
        String randomName = UUID.randomUUID().toString().replace("-", "");
        String fileType = Objects.requireNonNull(file.getContentType()).split("/")[1];

        if(!Arrays.asList(allowedFormats).contains(fileType)) {
            throw FileException.withStatusAndMessage(HttpStatus.BAD_REQUEST, ErrorMessages.UNSUPPORTED_FILE_TYPE);
        }

        String fileName = randomName + "." + fileType;

        try {
            File tempFile;

            if(fileType.equals("png") || fileType.equals("jpeg") || fileType.equals("jpg")) {
                BufferedImage originalImage = ImageIO.read(file.getInputStream());
                BufferedImage resizedImage = new BufferedImage(defaultImageWidth, defaultImageHeight, originalImage.getType());

                Graphics2D writer = resizedImage.createGraphics();
                writer.drawImage(originalImage, 0, 0, defaultImageWidth, defaultImageHeight, null);
                writer.dispose();

                tempFile = File.createTempFile(randomName, "." + fileType);
                ImageIO.write(resizedImage, fileType, tempFile);
            }
            else {
                tempFile = File.createTempFile(randomName, "." + fileType);
                file.transferTo(tempFile);
            }

            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, tempFile));

            tempFile.delete();

        } catch (IOException e) {
            throw FileException.withStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.FILE_CANNOT_WRITE);
        }

        FileEntity fileEntity = FileEntity.builder()
                .name(fileName)
                .type(file.getContentType())
                .cdnPath(cdnPath)
                .build();

        repository.save(fileEntity);

        return fileName;
    }

    @Override
    public byte[] getFile(String fileName) {

        Optional<FileEntity> response = repository.findByName(fileName);

        if(response.isPresent()) {

            try {
                String cdnUrl = cdnPath + fileName;
                URL url = new URI(cdnUrl).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();

                return IOUtils.toByteArray(inputStream);
            } catch (IOException | URISyntaxException e) {
                throw FileException.withStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.FILE_NOT_FOUND);
            }

        }
        else {
            throw FileException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.FILE_NOT_FOUND);
        }

    }

    @Override
    public Boolean deleteFile(String fileName) {

        Optional<FileEntity> response = repository.findByName(fileName);

        if(response.isPresent()){
            FileEntity existFile = response.get();
            repository.delete(existFile);

            try {
                amazonS3Client.deleteObject(bucketName, fileName);
            } catch (Exception e) {
                throw FileException.withStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.FILE_CANNOT_DELETE);
            }

            return true;
        }
        else {
            throw FileException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.FILE_NOT_FOUND);
        }

    }
}
