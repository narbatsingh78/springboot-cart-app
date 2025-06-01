package com.springbootcartapp.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springbootcartapp.controller.SseController;
import com.springbootcartapp.entities.CardItem;
import com.springbootcartapp.entities.SubCategory;
import com.springbootcartapp.entities.UploadTask;
import com.springbootcartapp.entities.User;
import com.springbootcartapp.repository.SelectedItemRepository;
import com.springbootcartapp.repository.SubCategoryRepository;
import com.springbootcartapp.repository.UploadTaskRepository;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class FileProcessingService {

    private final SelectedItemRepository selectedItemRepository;
    private final UploadTaskRepository uploadTaskRepository;
    private final SseController sseController;
    private final SubCategoryRepository subCategoryRepository;

    public FileProcessingService(SelectedItemRepository selectedItemRepository,UploadTaskRepository uploadTaskRepository,SseController sseController,SubCategoryRepository subCategoryRepository) {
        this.selectedItemRepository = selectedItemRepository;
        this.uploadTaskRepository = uploadTaskRepository;
        this.sseController = sseController;
        this.subCategoryRepository=subCategoryRepository;
    }

    @Async
    public void processFile(MultipartFile file, String taskId, User user) {
        try (InputStream inputStream = file.getInputStream()) {
            // Reading the Excel file using Apache POI
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);  // Assuming the data is in the first sheet
            Iterator<Row> rowIterator = sheet.iterator();

            List<CardItem> batch = new ArrayList<>();
            int count = 0;

            // Count total rows (first pass through the file)
            int total = 0;
            boolean isFirstRow = true;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // skip header
                }
                total++;
            }

            // Save initial UploadTask
            UploadTask task = new UploadTask();
            task.setTaskId(taskId);
            task.setUser(user);
            task.setTotalRecords(total);
            task.setInsertedRecords(0);
            task.setStatus("in_progress");
            task.setStartedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            uploadTaskRepository.save(task);

            // Reset the rowIterator and process the data
            rowIterator = sheet.iterator();
            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Skip the header row
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }

                // Assuming the data is structured correctly in Excel file
                CardItem cardItem = new CardItem();
                
                
                cardItem.setDescription(getCellValue(row.getCell(0)));
                cardItem.setImageName(getCellValue(row.getCell(1)));
                cardItem.setImagePath(getCellValue(row.getCell(2)));
                cardItem.setPrice(Double.parseDouble(getCellValue(row.getCell(3))));
                int priority=(int)(Double.parseDouble(getCellValue(row.getCell(4))));
                cardItem.setPriority(priority);
                int stockQuantity=(int)(Double.parseDouble(getCellValue(row.getCell(5))));
                cardItem.setStockQuantity(stockQuantity);
                cardItem.setTitle(getCellValue(row.getCell(6)));
                cardItem.setUserRating(Double.parseDouble(getCellValue(row.getCell(7))));
                int subCate=(int)(Double.parseDouble(getCellValue(row.getCell(8))));
                SubCategory subCategory = subCategoryRepository.findById(subCate).orElse(new SubCategory());
                cardItem.setSubCategory(subCategory);
                Double excelDate=Double.parseDouble(getCellValue(row.getCell(9)));
                LocalDate localDate=excelDateToLocalDate(excelDate);
                System.out.println("LocalDate..."+localDate);;
                cardItem.setProductLaunchDate(localDate);

                batch.add(cardItem);

                // Save batch if size reaches 1000, and send progress update
                if (batch.size() == 3) {
                    selectedItemRepository.saveAll(batch);
                    count += batch.size();
                    updateTask(task, count);  // Update task progress
                    batch.clear();
                }
                rowIndex++;
            }

            // Save the remaining records after processing all rows
            if (!batch.isEmpty()) {
                selectedItemRepository.saveAll(batch);
                count += batch.size();
                updateTask(task, count);
            }

            // Mark task as completed and finalize the task progress
            task.setStatus("completed");
            task.setUpdatedAt(LocalDateTime.now());
            uploadTaskRepository.save(task);

            // Send final completion status via SSE
            sseController.sendProgress(taskId, count, total, "completed");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTask(UploadTask task, int count) {
        task.setInsertedRecords(count);
        task.setUpdatedAt(LocalDateTime.now());
        uploadTaskRepository.save(task);
        sseController.sendProgress(task.getTaskId(), count, task.getTotalRecords(), "in_progress");
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    
    public static LocalDate excelDateToLocalDate(double excelDate) {
        // Excel date starts from 1900-01-01
        LocalDate epoch = LocalDate.of(1900, 1, 1);
        // Excel incorrectly considers 1900 as a leap year, so adjust if necessary
        if (excelDate > 60) {
            excelDate--; // Adjust for the leap year bug (Excel considers 1900 as a leap year)
        }
        return epoch.plusDays((long) excelDate - 1);  // Convert Excel serial date to LocalDate
    }
}
