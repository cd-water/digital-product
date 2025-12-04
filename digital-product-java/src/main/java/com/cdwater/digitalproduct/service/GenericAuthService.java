package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.model.auth.*;

public interface GenericAuthService<T> {
    LoginResponse login(LoginRequest loginRequest,Class<T> entityClass,long tokenExpiration);

    void register(RegisterRequest registerRequest,Class<T> entityClass);

    LoginResponse phoneLogin(LoginRequest loginRequest, Class<T> entityClass, long tokenExpiration);

    void forget(ForgetRequest forgetRequest, Class<T> entityClass);

    void change(ChangeRequest changeRequest, Class<T> entityClass);
}
