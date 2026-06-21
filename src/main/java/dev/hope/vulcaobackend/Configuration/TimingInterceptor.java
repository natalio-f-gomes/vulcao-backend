package dev.hope.vulcaobackend.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TimingInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(TimingInterceptor.class);
    private static final String ATTR = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        req.setAttribute(ATTR, System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res,
                                Object handler, Exception ex) {
        Object startAttr = req.getAttribute(ATTR);
        if (startAttr == null) {
            // preHandle didn't run (or another interceptor short-circuited); nothing to measure
            return;
        }
        long ms = (System.nanoTime() - (Long) startAttr) / 1_000_000;

        // Concise INFO line for normal monitoring
        log.info("{} {} -> {} ({} ms)",
                req.getMethod(), req.getRequestURI(), res.getStatus(), ms);

        // Detailed DEBUG line for troubleshooting
        if (log.isDebugEnabled()) {
            String queryString = req.getQueryString();
            log.debug(
                    "Request completed | method={} uri={} query={} status={} durationMs={} "
                            + "handler={} remoteAddr={} clientIp={} userAgent=\"{}\" "
                            + "reqContentType={} respContentType={} protocol={} exception={}",
                    req.getMethod(),
                    req.getRequestURI(),
                    queryString != null ? queryString : "-",
                    res.getStatus(),
                    ms,
                    describeHandler(handler),
                    req.getRemoteAddr(),
                    resolveClientIp(req),
                    req.getHeader("User-Agent"),
                    req.getContentType(),
                    res.getContentType(),
                    req.getProtocol(),
                    ex != null ? ex.getClass().getSimpleName() + ": " + ex.getMessage() : "none"
            );

            // If the request failed, log the full stack trace at DEBUG too
            if (ex != null) {
                log.debug("Exception detail for {} {}", req.getMethod(), req.getRequestURI(), ex);
            }
        }
    }

    /** Produces a readable "Controller#method" string from the handler, when available. */
    private String describeHandler(Object handler) {
        if (handler instanceof HandlerMethod hm) {
            return hm.getBeanType().getSimpleName() + "#" + hm.getMethod().getName();
        }
        return handler != null ? handler.getClass().getSimpleName() : "-";
    }

    /** Resolves the real client IP, honoring common proxy headers. */
    private String resolveClientIp(HttpServletRequest req) {
        String forwarded = req.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = req.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }
        return req.getRemoteAddr();
    }
}