package com.simplespringbootproject.config;

import com.springconfig.config.ShiroConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangmin on 2018/3/17.
 */
@Configuration
@ImportAutoConfiguration({
//        TomcatConfig.class,
        ShiroConfig.class
})
@ComponentScans({
        @ComponentScan(basePackages = {"com.simplespringbootproject.web"})
})
public class ImportCommonConfig {
}
