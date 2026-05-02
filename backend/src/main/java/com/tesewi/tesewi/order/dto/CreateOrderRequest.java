package com.tesewi.tesewi.order.dto;

import jakarta.validation.constraints.*;

public class CreateOrderRequest {

    @NotBlank(message = "Client name is required")
    @Size(max = 255, message = "Client name must not exceed 255 characters")
    private String clientName;

    @NotBlank(message = "Client phone is required")
    @Pattern(regexp = "^[0-9+\\-\\s()]{8,20}$", message = "Invalid phone format")
    private String clientPhone;

    @Email(message = "Invalid email format")
    private String clientEmail;

    @NotBlank(message = "Device type is required")
    @Size(max = 100, message = "Device type must not exceed 100 characters")
    private String deviceType;

    @Size(max = 100, message = "Device brand must not exceed 100 characters")
    private String deviceBrand;

    @Size(max = 100, message = "Device model must not exceed 100 characters")
    private String deviceModel;

    @NotBlank(message = "Issue description is required")
    private String issueDescription;

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
}