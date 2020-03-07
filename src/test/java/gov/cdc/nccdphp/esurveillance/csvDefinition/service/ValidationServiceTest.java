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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
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
        SimpleDateFormat formatter = new SimpleDateFormat("M/D/yyyy");

        CSVFile content = parse();
        Map<String, Object> metadata= new HashMap<>();
        metadata.put("ORGANIZATION_CODE", "293142");
        metadata.put("GRANTEE_ID", 34);
        metadata.put("FIRST_SESSION", formatter.parse("9/12/2017"));
        ValidationReport report = validationService.validate(content, metadata);
        System.out.println("report =\n " + report);
    }



    private CSVFile parse() throws InvalidDataException {
        try ( InputStream is =  getClass().getResourceAsStream("/testfile.csv")) {
            String content = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
            CSVFile file = transformer.parseContentAsCSVFile(content, true);
            System.out.println("file = " + file);
            return file;
        } catch (IOException e) {
            assert(false);
            throw new InvalidDataException(e.getMessage());

        }
    }

}