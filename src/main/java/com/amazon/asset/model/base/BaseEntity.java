package com.amazon.asset.model.base;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;
    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Version
    private int version;

}
