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
                individual.setBusiness_name(record[9]);
                individual.setFirst_name(record[37]);
                individual.setLast_name(record[43]);
                individual.setMaiden_name(record[45]);
                individual.setManager_name(record[46]);
                individual.setWhole_name(record[69]);
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
