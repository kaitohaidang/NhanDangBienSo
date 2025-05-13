package com.example.license_plate_recognition.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Cho phép tất cả origins trong quá trình phát triển (không nên dùng trong production)
        config.addAllowedOrigin("http://localhost:3000");
        
        // Cho phép tất cả methods (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");
        
        // Cho phép tất cả headers
        config.addAllowedHeader("*");
        
        // Cho phép gửi cookies (nếu cần)
        config.setAllowCredentials(true);
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
