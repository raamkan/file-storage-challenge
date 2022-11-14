package com.file.storage.service.filestorageservice;


import com.file.storage.service.filestorageservice.constants.FileStorageConstants;
import com.file.storage.service.filestorageservice.controller.FileStorageController;
import com.file.storage.service.filestorageservice.exception.CustomFileStorageExceptionHandler;
import com.file.storage.service.filestorageservice.model.FileData;
import com.file.storage.service.filestorageservice.service.FileStorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.file.storage.service.filestorageservice.constants.FileStorageConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileStorageServiceApplicationTests {

    @InjectMocks
    FileStorageController fileStorageController;

    @Mock
    FileStorageService fileStorageService;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        this.mockMvc =  MockMvcBuilders.standaloneSetup(fileStorageController)
                .setControllerAdvice(new CustomFileStorageExceptionHandler()).build();
    }



    @Test
    public void testFileUpLoadSuccessful() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", TEST_CSV_FILE,
                TEST_CONTENT_TYPE, getInputStream(TEST_CSV_FILE));
        FileData fileData = new FileData();
        int index = mockMultipartFile.getOriginalFilename().lastIndexOf('.');
        if(index > 0){
            String extension = mockMultipartFile.getOriginalFilename().substring(index + 1);
            fileData.setFileExtension(extension);
        }
        fileData.setFileName(mockMultipartFile.getOriginalFilename());
        fileData.setFileContent(mockMultipartFile.getBytes().toString());
        when(fileStorageService.saveFile(Mockito.any())).thenReturn(fileData);
        ResponseEntity<String> responseEntity = fileStorageController.create(mockMultipartFile);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }


    @Test
    public void test_File_upLoad_Error() throws Exception {
        ResponseEntity<String> responseEntity = fileStorageController.create(null);
        assertThat(responseEntity.getBody().toString()).isEqualTo(FileStorageConstants.NO_FILE_IN_THE_REQUEST);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void test_file_remove_from_inventory() throws Exception {
        when(fileStorageService.removeFile(Mockito.any())).thenReturn(1);
        ResponseEntity<String> responseEntity = fileStorageController.removeFile(TEST_CSV_FILE);
        assertThat(responseEntity.getBody().toString()).isEqualTo(FileStorageConstants.DELETED_IMAGE_SUCCESSFULLY);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void test_get_all_files_from_inventory() throws Exception {
        List<String> fileDataList = new ArrayList<>();
        fileDataList.add(TEST_CSV_FILE);
        fileDataList.add(TEST_XLSX_FILE);
        when(fileStorageService.allFiles()).thenReturn(fileDataList);
        List<String> response  = fileStorageService.allFiles();
        assertThat(response.size()).isEqualTo(fileDataList.size());
    }


    private byte[] getInputStream(String path) throws FileNotFoundException {
        byte[] bdata = null;
        ClassPathResource cpr = new ClassPathResource(path);
        try {
            bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
        } catch (IOException e) {
           System.out.println(e);
        }
        return bdata;
    }
}
