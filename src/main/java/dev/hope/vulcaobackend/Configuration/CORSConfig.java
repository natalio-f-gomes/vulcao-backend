package dev.hope.vulcaobackend.Configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CORSConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Exact origins only — never "*" when credentials are involved
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://vulcao.net",
                "https://www.vulcao.net"
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow any request header the browser asks for in preflight.
        // This is the cross-browser compatibility fix (Safari preflights fail
        // on unlisted headers) and is NOT a security hole — headers aren't
        // the attack surface; origins and credentials are.
        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        // Only expose what the frontend actually reads from responses
        configuration.setExposedHeaders(List.of("Authorization"));

        // Cache preflight results for 1 hour — fewer OPTIONS round-trips,
        // faster app on all browsers
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
