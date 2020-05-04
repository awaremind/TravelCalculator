package com.tivanov.travelmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(apiInfo())
        		.select()        
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))              
                .paths(PathSelectors.any())                          
                .build();   
    }
    
//    @Bean
//    public PluginRegistry<LinkDiscoverer, MediaType> discoverers(
//            OrderAwarePluginRegistry<LinkDiscoverer, MediaType> relProviderPluginRegistry) {
//        return relProviderPluginRegistry;
//    }

//    private Predicate<String travelPaths() {
//        return or(
//                regex("/travel/*")
//        );
//    }

    @SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Travel Calculaator API")
                .description("Spring  API reference for developers")
                .termsOfServiceUrl("http://localhost/")
                .contact("Tihomir Ivanov")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("3.0")
                .build();
    }

}