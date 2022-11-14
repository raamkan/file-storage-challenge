package com.file.storage.service.filestorageservice;


import com.file.storage.service.filestorageservice.constants.FileStorageConstants;
import com.file.storage.service.filestorageservice.model.FileData;
import com.file.storage.service.filestorageservice.repository.FileStorageRepository;
import com.file.storage.service.filestorageservice.serviceImpl.FileStorageServiceImpl;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.mockito.Mock;

@RunWith(MockitoJUnitRunner.class)
public class FileStorageServiceTests {

    @InjectMocks
    FileStorageServiceImpl fileStorageServiceImpl;

    @Mock
    FileStorageRepository mockFileStorageRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_found_file_and_upload_file() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));


        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", FileStorageConstants.TEST_CSV_FILE, FileStorageConstants.TEST_CONTENT_TYPE,
                getInputStream(FileStorageConstants.TEST_CSV_FILE));
        FileData fileData = new FileData();
        int index = mockMultipartFile.getOriginalFilename().lastIndexOf('.');
        if(index > 0){
            String extension = mockMultipartFile.getOriginalFilename().substring(index + 1);
            fileData.setFileExtension(extension);
        }
        fileData.setFileName(mockMultipartFile.getOriginalFilename());
        fileData.setFileContent(mockMultipartFile.getBytes().toString());
        when(mockFileStorageRepository.save(Mockito.any())).thenReturn(fileData);
        FileData response  = fileStorageServiceImpl.saveFile(mockMultipartFile);
        assertThat(response.getFileContent()).isNotEmpty();
        assertThat(response.getFileName()).isEqualTo(FileStorageConstants.TEST_CSV_FILE);
        assertThat(response.getFileExtension()).isEqualTo("csv");
    }


    @Test
    public void test_remove_file_from_inventory() throws Exception {
        when(mockFileStorageRepository.deleteByFileName(Mockito.any())).thenReturn(1);
        int response  = fileStorageServiceImpl.removeFile(FileStorageConstants.TEST_CSV_FILE);
        assertThat(response).isEqualTo(1);
    }


    @Test
    public void test_get_all_files() throws Exception {
        List<FileData> fileDataList = new ArrayList<>();
        fileDataList.add(new FileData(1, FileStorageConstants.TEST_FILE_EXTENSION_CSV, FileStorageConstants.TEST_DUMMY_CONTENT, FileStorageConstants.TEST_CSV_FILE));
        fileDataList.add(new FileData(2, "xlsx", FileStorageConstants.TEST_DUMMY_CONTENT, FileStorageConstants.TEST_XLSX_FILE));

        when(mockFileStorageRepository.findAll()).thenReturn(fileDataList);
        List<String> response  = fileStorageServiceImpl.allFiles();
        assertThat(response.size()).isEqualTo(fileDataList.size());
    }

    private byte[] getInputStream(String path) throws FileNotFoundException {
        byte[] bdata = null;
        ClassPathResource cpr = new ClassPathResource(path);
        try {
            bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
        return bdata;
    }
}
