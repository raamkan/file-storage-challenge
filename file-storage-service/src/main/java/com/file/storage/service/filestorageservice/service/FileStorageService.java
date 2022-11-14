package com.file.storage.service.filestorageservice.service;

import com.file.storage.service.filestorageservice.model.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileStorageService {

    public FileData saveFile(MultipartFile file) throws IOException;

    public int removeFile(String fileName);

    public List<String> allFiles();
}
