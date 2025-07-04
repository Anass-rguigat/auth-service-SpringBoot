package com.auth.demo.service;

import java.util.Map;

public interface AuthService {
    Map<String, Object> login(String grantType, 
                              String username, 
                              String password, 
                              boolean isWithRefreshToken, 
                              String refreshToken) throws Exception;
}
