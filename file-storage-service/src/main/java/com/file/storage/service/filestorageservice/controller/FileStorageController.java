package com.file.storage.service.filestorageservice.controller;

import com.file.storage.service.filestorageservice.constants.FileStorageConstants;
import com.file.storage.service.filestorageservice.exception.NoFileRecordFoundException;
import com.file.storage.service.filestorageservice.model.FileData;
import com.file.storage.service.filestorageservice.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "FileStorageController", description = "File Storage API with documentations")
public class FileStorageController {

    Logger logger = LoggerFactory.getLogger(FileStorageController.class);

    @Autowired
    FileStorageService fileStorageService;

    @Operation(summary = "upload file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "file uploaded", content = { @Content(mediaType = "application/form-data", schema = @Schema(implementation = MultipartFile.class))}),
            @ApiResponse(responseCode = "404", description = "No file to upload") })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> create(@RequestPart(value = "file", required=false) MultipartFile multipartFile) throws Exception {
        try{
            if(multipartFile == null || multipartFile.isEmpty()){
              logger.error("Error Code : ", HttpStatus.NOT_FOUND,  "Error Message : ",  FileStorageConstants.NO_FILE_IN_THE_REQUEST);
              return new ResponseEntity<>(FileStorageConstants.NO_FILE_IN_THE_REQUEST, HttpStatus.NOT_FOUND);
            }
           FileData file = fileStorageService.saveFile(multipartFile);
        }catch (Exception e){
            logger.error("Error Code : ", HttpStatus.INTERNAL_SERVER_ERROR,  "Error Message : ",  e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("File is successfully uploaded in inventory");
        return new ResponseEntity<String>(FileStorageConstants.SUCCESSFUL_FILE_UPLOAD, HttpStatus.OK);
    }

    @Operation(summary = "delete file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "file uploaded"),
            @ApiResponse(responseCode = "404", description = "No file found to delete")})
    @DeleteMapping(value = "/remove")
    public ResponseEntity<String> removeFile(@RequestParam(name="fileName") String fileName){
        if(fileStorageService.removeFile(fileName) == 0){
            logger.error("Error Code : ", HttpStatus.NOT_FOUND,  "Error Message : ", FileStorageConstants.NO_FILE_FOUND_IN_INVENTORY);
            throw new NoFileRecordFoundException(FileStorageConstants.NO_FILE_FOUND_IN_INVENTORY);
        }
        return new ResponseEntity<String>(FileStorageConstants.DELETED_IMAGE_SUCCESSFULLY, HttpStatus.OK);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "files")
            })
    @GetMapping(value = "/files")
    public ResponseEntity<List<String>> findFiles(){
        return new ResponseEntity<>(fileStorageService.allFiles(), HttpStatus.OK);
    }
}
