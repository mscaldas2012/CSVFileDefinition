package gov.cdc.nccdphp.esurveillance.csvDefinition;

import com.fasterxml.jackson.annotation.JsonView;
import gov.cdc.nccdphp.esurveillance.View;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;


/**
 * Created by marcelo on 10/7/16.
 */
@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="about")
//@ConfigurationProperties("about.yml")
public class About implements Serializable {
    @JsonView(View.Summary.class)
    private String summary;
    @JsonView(View.Summary.class)
    private List<ContactInfo> contacts;
    @JsonView(View.Summary.class)
    private List<String> versions ;
    @JsonView(View.Summary.class)
    private String docs;
    @JsonView(View.Summary.class)
    private String currentRelease;



    @Data
    @NoArgsConstructor
    public static class ContactInfo implements Serializable {
        @JsonView(View.Summary.class)
        private String name;
        @JsonView(View.Summary.class)
        private String email;
        @JsonView(View.Summary.class)
        private String role;

        public ContactInfo(String name, String email, String role) {
            this.name = name;
            this.email = email;
            this.role = role;
        }
    }
}
