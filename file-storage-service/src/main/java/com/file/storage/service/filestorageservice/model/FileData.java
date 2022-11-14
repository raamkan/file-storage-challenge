package com.file.storage.service.filestorageservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files_store")
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="file_extension")
    private String fileExtension;

    @Column(name="file_content")
    private String fileContent;

    @Column(name="file_name")
    private String fileName;
}
