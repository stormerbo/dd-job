package cn.ddlover.job.configuration;

import static cn.ddlover.job.constant.Roles.ADMIN;

import cn.ddlover.job.service.auth.AuthenticationHandler;
import cn.ddlover.job.service.auth.JobAuthenticationEntryPoint;
import cn.ddlover.job.service.auth.JobSessionFilter;
import cn.ddlover.job.service.auth.JobTokenRepository;
import cn.ddlover.job.service.auth.JobUserDetailsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.session.ConcurrentSessionFilter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/8 17:00
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean {

  @Autowired
  private JobAuthenticationEntryPoint jobAuthenticationEntryPoint;
  @Autowired
  private AuthenticationHandler authenticationHandler;
  @Autowired
  private JobUserDetailsService jobUserDetailsService;
  @Autowired
  private JobTokenRepository jobTokenRepository;
  @Autowired
  private JobSessionFilter jobSessionFilter;
  @Autowired
  private AdminProperties adminProperties;

  @Value("${spring.application.name}")
  private String applicationName;

  @Bean
  public HttpFirewall httpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowUrlEncodedPercent(true);
    return firewall;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(jobUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/admin/**").hasRole(ADMIN.role)
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginProcessingUrl("/login")
        .successHandler(authenticationHandler)
        .failureHandler(authenticationHandler)
        .permitAll()
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessHandler(authenticationHandler)
        .permitAll()
        .and()
        .rememberMe()
        .key(applicationName)
        .tokenRepository(jobTokenRepository)
        .and()
        .csrf()//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(jobAuthenticationEntryPoint)
        .and()
        // 会话管理，踢掉其他用户
        .sessionManagement().maximumSessions(1)
        .and().and()
        .addFilterAt(jobSessionFilter, ConcurrentSessionFilter.class);
  }


  @Override
  public void afterPropertiesSet() {
    // 重置或者更新
    if (!jobUserDetailsService.userExists(adminProperties.getUsername())) {
      UserDetails userDetails = User.withUsername(adminProperties.getUsername())
          .password(passwordEncoder().encode(adminProperties.getPassword()))
          .roles(ADMIN.role).build();
      jobUserDetailsService.createUser(userDetails);
    }
  }
}
