package com.myproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * Created by wangmin on 2018/3/16.
 * 不配置这个config时debug方式启动会找不到jsp文件，只能使用spring-boot:run
 */
@Configuration
public class TomcatConfig {

    @Value("${webapp.path}")
    private String webappPath;

    @Bean
    public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
        ConfigurableEmbeddedServletContainer factory = new TomcatEmbeddedServletContainerFactory();
        factory.setDocumentRoot(new File(webappPath));
        return (EmbeddedServletContainerFactory) factory;
    }
}
