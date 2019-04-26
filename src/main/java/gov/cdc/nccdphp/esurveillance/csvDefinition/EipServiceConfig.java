package gov.cdc.nccdphp.esurveillance.csvDefinition;


import com.fasterxml.jackson.annotation.JsonView;
import gov.cdc.nccdphp.esurveillance.View;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="eip-service-config")
public class EipServiceConfig {

    @JsonView(View.Summary.class)
    private PageInfo pageInfo;

    @Data
    @NoArgsConstructor
    public static class PageInfo {
        @JsonView(View.Summary.class)
        @Value("${pageInfo.defaultPageSize}")
        private int defaultPageSize;

        @JsonView(View.Summary.class)
        @Value("${pageInfo.maxPageSize}")
        private int maxPageSize;
    }
}
