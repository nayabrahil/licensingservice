package com.example.licensingservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
@Getter
public class ServiceConfig{

    @Value("${example.property}")

    private String exampleProperty;
}
