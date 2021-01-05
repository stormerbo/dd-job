package cn.ddlover.job.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/4 15:26
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final boolean postOnly = true;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    if (this.postOnly && !HttpMethod.POST.name().equals(request.getMethod())) {
      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }
    if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) || request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
      Map<String, String> loginData = new HashMap<>();
      try {
        loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
      String username = loginData.get(getUsernameParameter());
      String password = loginData.get(getPasswordParameter());
      if (username == null) {
        username = "";
      }
      if (password == null) {
        password = "";
      }
      username = username.trim();
      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
          username, password);
      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);
    } else{
      return super.attemptAuthentication(request, response);
    }
  }
}
