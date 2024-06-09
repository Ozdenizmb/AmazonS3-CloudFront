package com.amazon.asset.repository;

import com.amazon.asset.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {

    Optional<FileEntity> findByName(String fileName);

}
