package com.chandra.production.service;



import com.chandra.production.dto.LoginRequest;
import com.chandra.production.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);

    String login(LoginRequest request);
}