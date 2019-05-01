package gov.cdc.nccdphp.esurveillance.csvDefinition.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import gov.cdc.nccdphp.esurveillance.View;
import gov.cdc.nccdphp.esurveillance.csvDefinition.About;
import gov.cdc.nccdphp.esurveillance.csvDefinition.EipServiceConfig;
import gov.cdc.nccdphp.esurveillance.data.DataLoader;
import gov.cdc.nccdphp.esurveillance.rest.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by marcelo on 10/4/16.
 */

@RestController
@RequestMapping("/info/")
@ApiVersion({1})
public class InfoServiceController {
    @Autowired
    private DataLoader dataLoader;
    @Autowired
    private About about;

    @Autowired
    private EipServiceConfig config;

    @JsonView(View.Summary.class)
    @GetMapping(value="/about")
           // produces = {"application/cdc.info.about-v1+json"}) //This forces Safari to download the file instead of opening it on the browser.
    @ResponseBody
    public About about() throws JsonProcessingException {
        return about;
    }

    @GetMapping(value="/version")
    public String getVersion() {
        return "Version: " + getClass().getPackage().getImplementationVersion();
    }

    @GetMapping("/ping")
    public String ping() {
        return "Hello There! I'm alive.\nYou pinged me at " + ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
    }

    @JsonView(View.Summary.class)
    @GetMapping(value = "/config", produces = "application/json")
    public EipServiceConfig getConfig() {
        return config;
    }

    @PostMapping(value = "/loadData")
    public String loadData(@RequestBody String content) {
        dataLoader.loadDataSets(content);

        return "Value Sets Sucessfully Loaded";
    }

}

