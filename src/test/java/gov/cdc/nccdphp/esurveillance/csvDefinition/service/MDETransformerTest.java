package gov.cdc.nccdphp.esurveillance.csvDefinition.service;

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVFile;
import gov.cdc.nccdphp.esurveillance.rest.exceptions.InvalidDataException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @Created - 7/25/18
 * @Author Marcelo Caldas mcq1@cdc.gov
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MDETransformerTest {

    public static final String WW_MDE = "DPRP";
    public static final String VERSION = "1.0";

    @Autowired
    TransformerService transformer;

    @Test
    public void parseContent() throws InvalidDataException {
        parse();
    }

    @Test
    public void generateContent() throws InvalidDataException {
        CSVFile file = this.parse();
        String newFile = transformer.generateContent(WW_MDE, VERSION, file);
        System.out.println("New File generated...");
        System.out.println("newFile = \n\n" + newFile);
    }

    private CSVFile parse() throws InvalidDataException {
        InputStream is =  getClass().getClassLoader().getResourceAsStream("testFile.mde");
        String content = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
        CSVFile file = transformer.parseContentAsCSVFile(content);
        System.out.println("file = " + file);
        return file;
    }



}