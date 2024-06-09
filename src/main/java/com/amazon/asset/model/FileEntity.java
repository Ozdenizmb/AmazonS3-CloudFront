package com.amazon.asset.model;

import com.amazon.asset.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "asset_data", schema = "util_sch")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileEntity extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "cdn_path")
    private String cdnPath;

}
