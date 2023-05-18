package de.dlh.lhind.ecohack.service.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public interface ILogoutService extends LogoutHandler {
    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
