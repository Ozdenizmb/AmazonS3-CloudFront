package com.amazon.asset.repository;

import com.amazon.asset.model.ImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImagesEntity, UUID> {

    Optional<ImagesEntity> findByName(String fileName);

}
