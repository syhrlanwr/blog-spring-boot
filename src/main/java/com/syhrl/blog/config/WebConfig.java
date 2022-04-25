package com.syhrl.blog.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private final MessageSource messageSource;
    private final String[] RESOURCE_HANDLERS = {
            "/css/**",
            "/js/**",
            "/images/**",
            "/assets/**",
            "/static/**",
    };

    private final String[] RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/webjars/",
            "classpath:/static/css/",
            "classpath:/static/js/",
            "classpath:/static/images/",
            "classpath:/assets/"
    };

    public WebConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        registry.addResourceHandler(RESOURCE_HANDLERS).addResourceLocations(RESOURCE_LOCATIONS);
    }

    @Bean
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource);
        return factoryBean;
    }
}
