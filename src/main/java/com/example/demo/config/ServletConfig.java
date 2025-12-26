package com.example.demo.config;

import com.example.demo.servlet.SimpleStatusServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<SimpleStatusServlet> statusServlet() {
        ServletRegistrationBean<SimpleStatusServlet> registrationBean = 
            new ServletRegistrationBean<>(new SimpleStatusServlet());
        registrationBean.setUrlMappings(java.util.List.of("/status"));
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }
}   
