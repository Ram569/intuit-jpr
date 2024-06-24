package com.app.jpr.config.secuirty;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import java.io.IOException;

public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws ServletException, IOException {
        if (exception instanceof SessionAuthenticationException) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
