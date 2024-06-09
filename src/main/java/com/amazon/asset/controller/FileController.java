package com.amazon.asset.controller;

import com.amazon.asset.api.FileApi;
import com.amazon.asset.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class FileController implements FileApi {

    private final FileService service;

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file) {
        return ResponseEntity.ok(service.uploadFile(file));
    }

    @Override
    public ResponseEntity<byte[]> getFile(String fileName) {
        return ResponseEntity.ok(service.getFile(fileName));
    }

    @Override
    public ResponseEntity<Boolean> deleteFile(String fileName) {
        return ResponseEntity.ok(service.deleteFile(fileName));
    }

}
