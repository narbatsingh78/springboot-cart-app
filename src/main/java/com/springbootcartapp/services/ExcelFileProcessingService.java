package com.springbootcartapp.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.springbootcartapp.controller.SseController;
import com.springbootcartapp.entities.CardItem;
import com.springbootcartapp.entities.UploadTask;
import com.springbootcartapp.entities.User;
import com.springbootcartapp.repository.SelectedItemRepository;
import com.springbootcartapp.repository.SubCategoryRepository;
import com.springbootcartapp.repository.UploadTaskRepository;
import com.alibaba.excel.EasyExcel;
import java.io.*;
import java.time.LocalDateTime;


@Service
public class ExcelFileProcessingService {

    private final SelectedItemRepository selectedItemRepository;
    private final UploadTaskRepository uploadTaskRepository;
    private final SseController sseController;
    private final SubCategoryRepository subCategoryRepository;

    public ExcelFileProcessingService(SelectedItemRepository selectedItemRepository,UploadTaskRepository uploadTaskRepository,SseController sseController,SubCategoryRepository subCategoryRepository) {
        this.selectedItemRepository = selectedItemRepository;
        this.uploadTaskRepository = uploadTaskRepository;
        this.sseController = sseController;
        this.subCategoryRepository=subCategoryRepository;
    }

    @Async
    public void processExcelFile(MultipartFile file, String taskId, User user) {
        try (InputStream inputStream = file.getInputStream()) {
        	
        	int totalRows = getExcelRowCount(inputStream);
            inputStream.close(); // close to re-read later
           
            // Save initial UploadTask
            UploadTask task = new UploadTask();
            task.setTaskId(taskId);
            task.setUser(user);
            task.setTotalRecords(totalRows);
            task.setInsertedRecords(0);
            task.setStatus("in_progress");
            task.setStartedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            uploadTaskRepository.save(task);
            
            InputStream secondStream = file.getInputStream();
            EasyExcel.read(secondStream, CardItem.class,
                    new CardItemExcelListener(
                            selectedItemRepository,
                            subCategoryRepository,
                            uploadTaskRepository,
                            sseController,
                            task))
                    .sheet()
                    .doRead();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Utility to count data rows excluding header
    private int getExcelRowCount(InputStream inputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            return sheet.getPhysicalNumberOfRows() - 1; // excluding header
        }
    }

   
    
   
}
