package de.dlh.lhind.ecohack.service.security;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.response.LogoutDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public interface ILogoutService extends LogoutHandler {
    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
    LogoutDto successLogout() throws UnAuthorizedException;
}
