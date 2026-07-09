package br.com.lvb.studies.greeting_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

// Get configuration from application.yaml
// passing as parameter the properties name
// and can do biding with child properties
@ConfigurationProperties("greeting-service")
// This annotation allows to some beans can be refreshed in
// real time without restart the application
// Cant be used with java-records
@RefreshScope
public class GreetingConfiguration {

    private String greeting;

    private String defaultValue;

    public GreetingConfiguration() {}

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
