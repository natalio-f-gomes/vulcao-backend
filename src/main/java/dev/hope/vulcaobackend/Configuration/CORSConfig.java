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
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        // your frontend URL
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // allowed HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // allow all headers
        configuration.setAllowedHeaders(List.of("*"));
        // allows cookies (needed for your HttpOnly refresh token cookie)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // apply to all routes
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
