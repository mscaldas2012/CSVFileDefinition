package gov.cdc.nccdphp.esurveillance.rest;

import gov.cdc.nccdphp.esurveillance.rest.VersionRequestMappingHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by caldama on 10/6/16.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private ContentNegotiationManager contentNegotiationManager;

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        VersionRequestMappingHandlerMapping handlerMapping = new VersionRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setRemoveSemicolonContent(false);
        handlerMapping.setContentNegotiationManager(contentNegotiationManager);

        return handlerMapping;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/api/v2/api-docs", "/v2/api-docs");
        registry.addRedirectViewController("/api/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
        registry.addRedirectViewController("/api/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
        registry.addRedirectViewController("/api/swagger-resources", "/swagger-resources");
    }

    /**
     * @author hxo5 created on 10/05/2017
     * The following code is required for Swagger-ui to work as WebMvc conflicts with Srpingfox resource locations
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


}
