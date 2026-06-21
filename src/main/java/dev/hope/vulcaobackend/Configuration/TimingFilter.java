package dev.hope.vulcaobackend.Configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TimingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(TimingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {
        long start = System.nanoTime();
        try {
            chain.doFilter(req, res);
        } finally {
            long ms = (System.nanoTime() - start) / 1_000_000;

            // Concise INFO line for normal monitoring
            log.info("{} {} -> {} ms", req.getMethod(), req.getRequestURI(), ms);

            // Detailed DEBUG line for troubleshooting
            if (log.isDebugEnabled()) {
                String queryString = req.getQueryString();
                log.debug(
                        "Request completed | method={} uri={} query={} status={} durationMs={} "
                                + "remoteAddr={} clientIp={} userAgent=\"{}\" contentType={} "
                                + "reqContentLength={} respContentType={} protocol={} session={}",
                        req.getMethod(),
                        req.getRequestURI(),
                        queryString != null ? queryString : "-",
                        res.getStatus(),
                        ms,
                        req.getRemoteAddr(),
                        resolveClientIp(req),
                        req.getHeader("User-Agent"),
                        req.getContentType(),
                        req.getContentLengthLong(),
                        res.getContentType(),
                        req.getProtocol(),
                        req.getRequestedSessionId() != null ? req.getRequestedSessionId() : "-"
                );
            }
        }
    }

    /** Resolves the real client IP, honoring common proxy headers. */
    private String resolveClientIp(HttpServletRequest req) {
        String forwarded = req.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            // X-Forwarded-For can be a comma-separated list; the first entry is the original client
            return forwarded.split(",")[0].trim();
        }
        String realIp = req.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }
        return req.getRemoteAddr();
    }
}