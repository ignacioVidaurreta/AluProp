package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.APUserDetailsAuthenticationProvider;
import ar.edu.itba.paw.webapp.auth.APUserDetailsService;
import ar.edu.itba.paw.webapp.auth.LoginSuccessHandler;
import ar.edu.itba.paw.webapp.config.filter.LoginAuthFilter;
import ar.edu.itba.paw.webapp.config.filter.SessionAuthFilter;
import ar.edu.itba.paw.webapp.config.handler.LoginAuthFailureHandler;
import ar.edu.itba.paw.webapp.config.handler.LoginAuthSuccessHandler;
import ar.edu.itba.paw.webapp.config.handler.SessionAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private APUserDetailsService userDetailsService;
    @Autowired
    private APUserDetailsAuthenticationProvider authenticationProvider;
    @Autowired
    private LoginAuthSuccessHandler successHandler;
    @Autowired
    private LoginAuthFailureHandler failureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService)
                .addFilterBefore(createLoginAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(createSessionAuthFilter(), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().logout().disable()
                .rememberMe().disable()
                .csrf().disable();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter createLoginAuthFilter() throws Exception {
        LoginAuthFilter filter = new LoginAuthFilter();
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    public AbstractAuthenticationProcessingFilter createSessionAuthFilter() throws Exception {
        SessionAuthFilter filter = new SessionAuthFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setRequiresAuthenticationRequestMatcher(userMatcher());
        filter.setAuthenticationSuccessHandler(new SessionAuthSuccessHandler());
        return filter;
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/403");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new LoginSuccessHandler("/");
    }

    @Bean
    public RequestMatcher hostMatcher() {
        return new OrRequestMatcher(
            new AntPathRequestMatcher("/api/host/**", "POST"),
            new AntPathRequestMatcher("/api/host/**", "GET")
        );
    }

    @Bean
    public RequestMatcher guestMatcher() {
        return new OrRequestMatcher(
            new AntPathRequestMatcher("/api/guest/**", "POST"),
            new AntPathRequestMatcher("/api/guest/**", "GET")
        );
    }


    @Bean
    public RequestMatcher anonymousMatcher() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/api/auth/signup", "POST"),
                new NegatedRequestMatcher(userMatcher())
        );
    }

    @Bean
    public RequestMatcher userMatcher() {
        return new OrRequestMatcher(
            hostMatcher(),
            guestMatcher(),
            logoutMatcher(),
            new AntPathRequestMatcher("/api/user/**", "POST"),
            new AntPathRequestMatcher("/api/user/**", "GET"),
            new AntPathRequestMatcher("/api/proposal/**", "POST"),
            new AntPathRequestMatcher("/api/proposal/**", "GET")
        );
    }

    @Bean
    public RequestMatcher logoutMatcher() {
        return new AntPathRequestMatcher("/api/auth/logout");
    }
}
