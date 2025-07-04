package com.auth.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String status; // "success" or "error"
    private String message;
    private Map<String, Object> data; // optional data for success
    private Integer errorCode; 
}
