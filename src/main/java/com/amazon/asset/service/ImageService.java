package com.amazon.asset.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile file);
    byte[] getImage(String fileName);
    Boolean deleteImage(String fileName);

}
