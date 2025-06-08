package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class cloudinaryConfig {
    
    @Bean
    public Cloudinary getCloudinary() {

        Map<String, Object> config = new HashMap<>();

        config.put("cloud_name", "dx50xbegd");
        config.put("api_key", "135553742698344");
        config.put("api_secret", "b6yLKIl4fVC4i1as6bGDF6XjE1E");
        config.put("secure", true);

        return new Cloudinary(config);
    }
}
