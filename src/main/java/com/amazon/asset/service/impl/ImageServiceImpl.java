package com.amazon.asset.service.impl;

import com.amazon.asset.repository.ImageRepository;
import com.amazon.asset.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository repository;

    @Override
    public String uploadImage(MultipartFile file) {
        return "";
    }

    @Override
    public byte[] getImage(String fileName) {
        return new byte[0];
    }

    @Override
    public Boolean deleteImage(String fileName) {
        return null;
    }
}
