package kimosabe.api.config;

import kimosabe.api.constants.AppConstants;
import kimosabe.api.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.Session;

@Configuration
@SuppressWarnings("unused")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig<S extends Session> extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userService;
    private final FilterConfig filterConfig;
    private final SessionRegistry sessionRegistry;

    @Autowired
    public WebSecurityConfig(
            PasswordEncoder passwordEncoder,
            CustomUserDetailsService userService,
            FilterConfig filterConfig,
            SessionRegistry sessionRegistry
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.filterConfig = filterConfig;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/games/**", "/groups/{groupId}", "/user/profile/**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterAt(filterConfig.usernamePasswordAuthenticationFilter(authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
                .sessionManagement().maximumSessions(AppConstants.MAX_NUM_SESSIONS).sessionRegistry(sessionRegistry);
    }
}
