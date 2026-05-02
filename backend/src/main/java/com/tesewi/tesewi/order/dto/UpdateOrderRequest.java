package com.tesewi.tesewi.order.dto;

import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class UpdateOrderRequest {

    @Size(max = 255, message = "Client name must not exceed 255 characters")
    private String clientName;

    @Size(max = 20, message = "Client phone must not exceed 20 characters")
    private String clientPhone;

    private String clientEmail;

    @Size(max = 100, message = "Device type must not exceed 100 characters")
    private String deviceType;

    @Size(max = 100, message = "Device brand must not exceed 100 characters")
    private String deviceBrand;

    @Size(max = 100, message = "Device model must not exceed 100 characters")
    private String deviceModel;

    private String issueDescription;

    private String technicianNotes;

    private BigDecimal estimatedCost;

    private BigDecimal finalCost;

    // Getters and Setters
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getTechnicianNotes() {
        return technicianNotes;
    }

    public void setTechnicianNotes(String technicianNotes) {
        this.technicianNotes = technicianNotes;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public BigDecimal getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(BigDecimal finalCost) {
        this.finalCost = finalCost;
    }
}