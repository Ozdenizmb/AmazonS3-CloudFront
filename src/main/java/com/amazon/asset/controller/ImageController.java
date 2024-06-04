package com.amazon.asset.controller;

import com.amazon.asset.api.ImageApi;
import com.amazon.asset.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class ImageController implements ImageApi {

    private final ImageService service;

    @Override
    public ResponseEntity<String> uploadImage(MultipartFile file) {
        return ResponseEntity.ok(service.uploadImage(file));
    }

    @Override
    public ResponseEntity<byte[]> getImage(String fileName) {
        return ResponseEntity.ok(service.getImage(fileName));
    }

    @Override
    public ResponseEntity<Boolean> deleteImage(String fileName) {
        return ResponseEntity.ok(service.deleteImage(fileName));
    }

}
