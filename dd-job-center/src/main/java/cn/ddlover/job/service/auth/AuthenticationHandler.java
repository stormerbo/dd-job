package cn.ddlover.job.service.auth;

import cn.ddlover.job.entity.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/8 17:18
 */
@Service
public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication)
      throws IOException {
    Object principal = authentication.getPrincipal();
    resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
    PrintWriter out = resp.getWriter();
    out.write(new ObjectMapper().writeValueAsString(Response.successWithData(principal)));
    out.flush();
    out.close();
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e)
      throws IOException {
    resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
    PrintWriter out = resp.getWriter();
    out.write(new ObjectMapper().writeValueAsString(Response.fail("用户名/密码错误")));
    out.flush();
    out.close();
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication)
      throws IOException {
    resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
    PrintWriter out = resp.getWriter();
    out.write(new ObjectMapper().writeValueAsString(Response.successWithMessage("注销成功")));
    out.flush();
    out.close();
  }
}
