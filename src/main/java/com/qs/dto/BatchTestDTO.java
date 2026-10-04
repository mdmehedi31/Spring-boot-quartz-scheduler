package com.qs.dto;

public class BatchTestDTO {

    private String message;

    public BatchTestDTO(String message) {
        this.message = message;
    }

    public BatchTestDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
