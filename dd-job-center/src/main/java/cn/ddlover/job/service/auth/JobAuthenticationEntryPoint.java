package cn.ddlover.job.service.auth;

import cn.ddlover.job.constant.ResponseCode;
import cn.ddlover.job.entity.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/9 16:59
 *
 *
 * 未登录时走的地方，统一返回需要登录的信息
 */
@Component
public class JobAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse resp, AuthenticationException authException)
      throws IOException {
    resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
    PrintWriter out = resp.getWriter();
    Response<Void> voidResponse = new Response<>(ResponseCode.NOT_LOGIN);
    out.write(new ObjectMapper().writeValueAsString(voidResponse));
    out.flush();
    out.close();
  }
}
