package com.tesewi.tesewi.order.dto;

import com.tesewi.tesewi.order.entity.OrderPhoto;
import java.time.LocalDateTime;

public class PhotoResponse {

    private Long id;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadedAt;

    public static PhotoResponse fromEntity(OrderPhoto photo) {
        PhotoResponse response = new PhotoResponse();
        response.setId(photo.getId());
        response.setFileName(photo.getFileName());
        response.setFilePath(photo.getFilePath());
        response.setUploadedAt(photo.getUploadedAt());
        return response;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}