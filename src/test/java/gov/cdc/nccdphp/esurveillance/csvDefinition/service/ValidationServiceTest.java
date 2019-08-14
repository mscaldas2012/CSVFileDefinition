package gov.cdc.nccdphp.esurveillance.csvDefinition.service;

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVFile;
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValidationReport;
import gov.cdc.nccdphp.esurveillance.rest.exceptions.InvalidDataException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @Created - 2019-03-16
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("LOCAL")
public class ValidationServiceTest {



    public static final String WW_MDE = "DPRP";
    public static final String VERSION = "1.0";

    @Autowired
    TransformerService transformer;
    @Autowired
    private Validator validationService;

    @Before
    public void init() {
        this.validationService.configure(WW_MDE, VERSION);
    }
    @Test
    public void validate() throws Exception {
        CSVFile content = parse();
        ValidationReport report = validationService.validate(content);
        System.out.println("report =\n " + report);
    }



    private CSVFile parse() throws InvalidDataException {
        InputStream is =  getClass().getClassLoader().getResourceAsStream("testfile.csv");
        String content = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
        CSVFile file = transformer.parseContentAsCSVFile( content, true);
        System.out.println("file = " + file);
        return file;
    }

}