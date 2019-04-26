package gov.cdc.nccdphp.esurveillance.data;

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValueSet;
import gov.cdc.nccdphp.esurveillance.csvDefinition.repository.ValueSetMongoRepo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collections;
import java.util.Scanner;

/**
 * @Created - 7/23/18
 * @Author Marcelo Caldas mcq1@cdc.gov
 */

@Component
public class DataLoader {
    @Autowired
    ValueSetMongoRepo valueSetRepo;

    private Log log = LogFactory.getLog(DataLoader.class);


    public void loadDataSets(String filename) {
        InputStream file = getClass().getClassLoader().getResourceAsStream(filename);
        try (Scanner scanner = new Scanner(file)) {
            scanner.nextLine(); //skip first line
            //Create MMG ->
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");



                ValueSet valueSet = new ValueSet(values[0], Collections.emptyList());
                for (int i = 1; i < values.length; i++) { //Add all possible value Sets:
                    String[] vs_code = values[i].split("\\^");
                    valueSet.addChoice(vs_code[0], vs_code[1]);
                }
                valueSetRepo.save(valueSet);
            }
        }
    }

    private Double getDoubleValue(String value) {
        if (value == null || value.trim().length() == 0) {
            return 0.0;
        } else {
            return new Double(value);
        }
    }
}
