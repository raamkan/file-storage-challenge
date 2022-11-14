package com.file.storage.service.filestorageservice.serviceImpl;


import com.file.storage.service.filestorageservice.model.FileData;
import com.file.storage.service.filestorageservice.repository.FileStorageRepository;
import com.file.storage.service.filestorageservice.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileStorageServiceImpl implements FileStorageService {
    @Autowired
    FileStorageRepository fileStorageRepository;

    @Override
    public FileData saveFile(MultipartFile file) throws IOException {
        System.out.println("Inside Service Impl.....");
        FileData fileData = new FileData();
        int index = file.getOriginalFilename().lastIndexOf('.');
        if(index > 0){
            String extension = file.getOriginalFilename().substring(index + 1);
            fileData.setFileExtension(extension);
        }
        fileData.setFileName(file.getOriginalFilename());
        fileData.setFileContent(file.getBytes().toString());
        FileData data = fileStorageRepository.save(fileData);
        return data;
    }

    @Override
    public int removeFile(String fileName) {
        return fileStorageRepository.deleteByFileName(fileName);
    }

    @Override
    public List<String> allFiles() {
        List<FileData> fileDataList = fileStorageRepository.findAll();
        return fileDataList.stream().map(FileData::getFileName).collect(Collectors.toList());
    }
}
