package com.amazon.asset.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(MultipartFile file);
    byte[] getFile(String fileName);
    String getCDNUrlForFile(String fileName);
    Boolean deleteFile(String fileName);

}
