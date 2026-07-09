package br.com.lvb.studies.greeting_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Get configuration from application.yaml
// passing as parameter the properties name
// and can do biding with child properties
@ConfigurationProperties("greeting-service")
public record GreetingConfiguration (String greeting, String defaultValue) {}
