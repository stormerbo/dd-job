package cn.ddlover.job.service.auth;

import cn.ddlover.job.constant.ResponseCode;
import cn.ddlover.job.entity.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.stereotype.Service;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/10 15:54
 */
@Service
public class JobSessionFilter extends ConcurrentSessionFilter {

  @Autowired
  public JobSessionFilter(SessionRegistry sessionRegistry) {
    super(sessionRegistry, event -> {
      HttpServletResponse resp = event.getResponse();
      resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
      resp.setStatus(401);
      PrintWriter out = resp.getWriter();
      Response<Void> voidResponse = new Response<>(ResponseCode.LOGIN_OTHER);
      out.write(new ObjectMapper().writeValueAsString(voidResponse));
      out.flush();
      out.close();
    });
  }
}
