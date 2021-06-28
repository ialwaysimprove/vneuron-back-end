package OpenCSVParser;


import com.opencsv.CSVReader;
import com.vneuron.sbelasticback.Postgres.Customer;

import java.io.FileReader;
import java.util.ArrayList;


public class customersLoader {

    public static ArrayList<Customer> customerCSVParser() {

        String fileName = "src/main/resources/csv/exportCustomers.csv";
        try (CSVReader reader = new CSVReader(new FileReader(fileName), '\t')) {

            String[] record = new String[90];
            ArrayList<Customer> customerList = new ArrayList<>();
            reader.readNext(); // The first line is the columns line
            while ((record = reader.readNext()) != null) {

                Customer individual = new Customer();
                // System.out.println("Exception After Here 1 "); // The error was because the first line holds the field name
                individual.setId(Long.parseLong(record[0]));
                // System.out.println("Exception After Here 2 ");
                individual.setBusiness_name(record[9].trim().isEmpty() || record[9].trim().equals("null")? "": record[9].trim());
                individual.setFirst_name(record[37].trim().isEmpty() || record[37].trim().equals("null")? "": record[37].trim());
                individual.setLast_name(record[43].trim().isEmpty() || record[43].trim().equals("null")? "": record[43].trim());
                individual.setMaiden_name(record[45].trim().isEmpty() || record[45].trim().equals("null")? "": record[45].trim());
                individual.setManager_name(record[46].trim().isEmpty() || record[46].trim().equals("null")? "": record[46].trim());
                individual.setWhole_name(record[69].trim().isEmpty() || record[69].trim().equals("null")? "": record[69].trim());
                // individual.setGazette_ref(record[40]);
                customerList.add(individual);

            }

            return customerList;
        }
        catch (Exception exception) {
            System.out.println("An Exception in CSV Parser");
        }
        return null;
    }
}
