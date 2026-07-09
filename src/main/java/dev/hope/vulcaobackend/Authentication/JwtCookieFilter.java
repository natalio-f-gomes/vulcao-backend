package dev.hope.vulcaobackend.Authentication;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class JwtCookieFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger log = LoggerFactory.getLogger(JwtCookieFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        Cookie[] cookies = Optional.ofNullable(req.getCookies()).orElse(new Cookie[0]);

        // How many AUTH_TOKEN cookies arrived? >1 means a stale duplicate
        // (same name, different Path/Domain) — the classic "old token keeps coming back"
        List<String> authTokens = Arrays.stream(cookies)
                .filter(c -> "AUTH_TOKEN".equals(c.getName()))
                .map(Cookie::getValue)
                .toList();

        log.debug("{} {} — cookies: {}, AUTH_TOKEN count: {}",
                req.getMethod(), req.getRequestURI(),
                Arrays.stream(cookies).map(Cookie::getName).toList(),
                authTokens.size());

        if (authTokens.size() > 1) {
            log.warn("MULTIPLE AUTH_TOKEN cookies received ({}). Browser is sending a stale duplicate — "
                    + "clearCookie/createCookie Path or Domain mismatch.", authTokens.size());
        }

        String token = authTokens.isEmpty() ? null : authTokens.get(0);

        try {
            if (token == null) {
                log.debug("No AUTH_TOKEN cookie — proceeding anonymous");
            } else if (!jwtUtil.isTokenValid(token)) {
                log.warn("AUTH_TOKEN present but INVALID for {} {} (see JWTService log above for reason)",
                        req.getMethod(), req.getRequestURI());
            } else {
                String username = jwtUtil.extractUsername(token);
                log.debug("Token valid, loading user '{}'", username);
                UserDetails user = userDetailsService.loadUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug("Authenticated '{}' with authorities {}", username, user.getAuthorities());
            }
        } catch (UsernameNotFoundException e) {
            log.warn("Token was valid but user '{}' not found in DB — deleted account with live cookie?",
                    e.getMessage());
            SecurityContextHolder.clearContext();
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT processing failed: {} - {}", e.getClass().getSimpleName(), e.getMessage());
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(req, res);
    }
}