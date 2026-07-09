package dev.hope.vulcaobackend.Configuration;

import dev.hope.vulcaobackend.Authentication.JwtCookieFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    private JwtCookieFilter jwtFilter;
    @Autowired
    private CustomUserDetailService userDetailService;
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("==== Building Security Filter Chain ====");
        log.info("CSRF: DISABLED (stateless JWT API)");
        log.info("CORS: Configured via CorsConfigurationSource");
        log.info("Session: STATELESS");
        log.info("Public paths: /api/v2/auth/**, /api/v1/public/**, /api/v1/actuator/**");


        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/error",
                                "/api/v2/auth/**",
                                "/api/v2/public/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(requestLoggingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Logs every incoming request with method, path, status, cookies, and auth state.
     * Assigns a request ID so you can correlate logs from one request.
     * Output example:
     * [a3f2b1] >> PUT /api/v1/user/update/nataliogomes/address
     * [a3f2b1]    cookies: [JWT_TOKEN, XSRF-TOKEN]
     * [a3f2b1]    headers: Content-Type=application/json, X-XSRF-TOKEN=present
     * [a3f2b1] << 403 (took 12ms) auth=null
     */
    @Bean
    public OncePerRequestFilter requestLoggingFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain)
                    throws ServletException, IOException {

                String requestId = UUID.randomUUID().toString().substring(0, 8);
                long start = System.currentTimeMillis();

                // Inbound log
                log.info("[{}] >> {} {}{}",
                        requestId,
                        req.getMethod(),
                        req.getRequestURI(),
                        req.getQueryString() != null ? "?" + req.getQueryString() : "");


                // Cookies
                Cookie[] cookies = req.getCookies();
                if (cookies != null && cookies.length > 0) {
                    String cookieNames = Arrays.stream(cookies)
                            .map(Cookie::getName)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("");
                    log.info("[{}]    cookies: [{}]", requestId, cookieNames);
                } else {
                    log.info("[{}]    cookies: (none)", requestId);
                }

                // Important headers
                String contentType = req.getHeader("Content-Type");
                String origin = req.getHeader("Origin");
                String userAgent = req.getHeader("User-Agent");
                log.info("[{}]    Content-Type={}, Origin={}, UA={}",
                        requestId, contentType, origin,
                        userAgent != null && userAgent.length() > 40
                                ? userAgent.substring(0, 40) + "..."
                                : userAgent);

                try {
                    chain.doFilter(req, res);
                } finally {
                    long took = System.currentTimeMillis() - start;
                    String authName = org.springframework.security.core.context.SecurityContextHolder
                            .getContext().getAuthentication() != null
                            ? org.springframework.security.core.context.SecurityContextHolder
                              .getContext().getAuthentication().getName()
                            : "anonymous";
                    log.info("[{}] << {} (took {}ms) auth={}",
                            requestId, res.getStatus(), took, authName);
                }
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        log.info("DaoAuthenticationProvider initialized with CustomUserDetailService + BCrypt");
        return provider;
    }
}