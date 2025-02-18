package com.procesos.colas.application;

import com.procesos.colas.Infrastructure.Adapter.LoginAdapter;
import com.procesos.colas.application.Dto.LoginCamerResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginAdapter loginAdapter;

    public LoginServiceImpl(LoginAdapter loginAdapter) {
        this.loginAdapter = loginAdapter;
    }

    @Override
    public LoginCamerResponse login() {
        return loginAdapter.login();
    }
}