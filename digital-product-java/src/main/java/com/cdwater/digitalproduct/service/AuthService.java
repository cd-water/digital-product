package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.model.auth.*;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);

    String sendCode(CodeRequest codeRequest);

    LoginResponse phoneLogin(LoginRequest loginRequest);

    void register(RegisterRequest registerRequest);

    void forget(ForgetRequest forgetRequest);

    void change(ChangeRequest changeRequest);

    void logout(HttpServletRequest request);
}
