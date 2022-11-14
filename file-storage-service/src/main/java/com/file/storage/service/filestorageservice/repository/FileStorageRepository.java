package com.file.storage.service.filestorageservice.repository;

import com.file.storage.service.filestorageservice.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileStorageRepository extends JpaRepository<FileData, Integer> {

    int deleteByFileName(String fileName);
}
