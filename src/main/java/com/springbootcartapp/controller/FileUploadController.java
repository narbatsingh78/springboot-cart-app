package com.springbootcartapp.controller;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springbootcartapp.entities.CustomUserDetails;
import com.springbootcartapp.entities.User;
import com.springbootcartapp.repository.UserRepository;
import com.springbootcartapp.services.ExcelFileProcessingService;
import com.springbootcartapp.services.FileProcessingService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class FileUploadController {

    private final FileProcessingService fileProcessingService;
    private final UserRepository userRepository;
    private final ExcelFileProcessingService excelFileProcessingService;

    public FileUploadController(FileProcessingService fileProcessingService,UserRepository userRepository,ExcelFileProcessingService excelFileProcessingService) {
        this.fileProcessingService = fileProcessingService;
        this.userRepository=userRepository;
        this.excelFileProcessingService=excelFileProcessingService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,@RequestPart("taskId") String taskId, @AuthenticationPrincipal  CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();
        User user = userRepository.findByUserEmail(username).orElseThrow();

//        String taskId = UUID.randomUUID().toString();

        fileProcessingService.processFile(file, taskId, user);
        
//        excelFileProcessingService.processExcelFile(file, taskId, user);

        return ResponseEntity.ok(Map.of("taskId", taskId));
    }
    
    
}
