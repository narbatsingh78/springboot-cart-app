package com.springbootcartapp.services;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.springbootcartapp.entities.*;
import com.springbootcartapp.repository.*;
import com.springbootcartapp.controller.SseController;

import java.util.ArrayList;
import java.util.List;




public class CardItemExcelListener extends AnalysisEventListener<CardItem> {

	private final SelectedItemRepository selectedItemRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final UploadTaskRepository uploadTaskRepository;
    private final SseController sseController;
    private final UploadTask task;

    private final int batchSize = 10;
    private final List<CardItem> buffer = new ArrayList<>();
    private int insertedCount = 0;

    public CardItemExcelListener(
            SelectedItemRepository selectedItemRepository,
            SubCategoryRepository subCategoryRepository,
            UploadTaskRepository uploadTaskRepository,
            SseController sseController,
            UploadTask task
        ) {
            this.selectedItemRepository = selectedItemRepository;
            this.subCategoryRepository = subCategoryRepository;
            this.uploadTaskRepository = uploadTaskRepository;
            this.sseController = sseController;
            this.task = task;
        }
    @Override
    public void invoke(CardItem cardItem, AnalysisContext context) {
        try {
            // Map sub-category
//        	if (cardItem.getSubCategoryId() != null) {
//                SubCategory subCategory = subCategoryRepository.findById(cardItem.getSubCategoryId())
//                    .orElse(null);
//                cardItem.setSubCategory(subCategory);
//            }

//            // Parse product launch date if needed
//            if (cardItem.getProductLaunchDate() != null && !cardItem.getProductLaunchDate().isBlank()) {
//                LocalDate launchDate = LocalDate.parse(cardItem.getProductLaunchDate()); // or use formatter
//                cardItem.setProductLaunchDate(launchDate); // add new field
//            }

            buffer.add(cardItem);

            if (buffer.size() >= batchSize) {
                saveBatch();
            }

        } catch (Exception e) {
            e.printStackTrace(); // log and skip row
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveBatch(); // final flush
        task.setStatus("completed");
        task.setInsertedRecords(insertedCount);
        task.setUpdatedAt(java.time.LocalDateTime.now());
        uploadTaskRepository.save(task);

        sseController.sendProgress(task.getTaskId(), insertedCount, task.getTotalRecords(), "completed");
    }

    private void saveBatch() {
        selectedItemRepository.saveAll(buffer);
        insertedCount += buffer.size();
        buffer.clear();

        task.setInsertedRecords(insertedCount);
        task.setUpdatedAt(java.time.LocalDateTime.now());
        uploadTaskRepository.save(task);
        sseController.sendProgress(task.getTaskId(), insertedCount, task.getTotalRecords(), "in_progress");
    }
}

