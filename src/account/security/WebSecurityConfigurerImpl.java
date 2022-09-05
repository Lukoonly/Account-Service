package account.security;

import account.security.handler.CustomAccessDeniedHandler;
import account.security.handler.CustomAuthenticationEntryPointHandler;
import account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .mvcMatchers("/api/security/events").hasRole("AUDITOR")
                .mvcMatchers("api/acct/payments", "api/acct/payments").hasRole("ACCOUNTANT")
                .mvcMatchers("api/empl/payment").hasAnyRole("ACCOUNTANT", "USER")
                .mvcMatchers("api/auth/changepass").hasAnyRole("ADMINISTRATOR", "USER", "ACCOUNTANT")
                .mvcMatchers("/api/admin/**", "api/security/events").hasRole("ADMINISTRATOR")
                .mvcMatchers("/", "api/auth/signup", "/h2-console", "/actuator/shutdown").permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(getAccessDeniedHandler())
                .and()
                .httpBasic()
                .and()
                .csrf().disable()

                .headers().frameOptions().disable()
                .and()
                .httpBasic().authenticationEntryPoint(getAuthenticationEntryPoint())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPointHandler();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(getEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.accountService);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}