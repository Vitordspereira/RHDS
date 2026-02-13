package com.hub.hds.configuracao.video;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/videos/**")
                .addResourceLocations(
                        "file:C:/Users/user/Desktop/@Vitor - estratégia/projetos/RHDS-Back-End/uploads/videos/"
                );
    }
}
