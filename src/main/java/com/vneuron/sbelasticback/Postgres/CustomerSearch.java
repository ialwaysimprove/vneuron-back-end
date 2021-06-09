package com.vneuron.sbelasticback.Postgres;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vneuron.sbelasticback.Elasticsearch.SearchController;
import com.vneuron.sbelasticback.Elasticsearch.Watchlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CustomerSearch {

    @Autowired
    SearchController controller;

    public List<CWJTable> checkCustomers(List<Customer> customersToCheck) {
        // try {
        // FileWriter myWriter = new FileWriter("src/main/resources/output/output.txt");
        HashMap<String, SearchHits<Watchlist>> customersWithCorrespondingMatches = new HashMap<>();
        List<CWJTable> clientWatchlistList= new ArrayList<>();
        for (Customer customer : customersToCheck) {
            ObjectMapper mapper = new ObjectMapper();
            List<String> namesToSearch = new ArrayList<>();
            namesToSearch.add(customer.getWhole_name());
            namesToSearch.add(customer.getBusiness_name());
            namesToSearch.add(customer.getFirst_name() + ' ' + customer.getLast_name() + ' ' + customer.getMaiden_name());
            namesToSearch.add(customer.getManager_name() + ' ' + customer.getGazette_ref());

            SearchHits<Watchlist> results = controller.checkName(namesToSearch);


            results.forEach(
                    element -> {
//                            ObjectNode commonFormat = mapper.createObjectNode();
//                            commonFormat.put("customer_id", customer.getId());
//                            commonFormat.put("watchlist_id", element.getId());
//                            commonFormat.put("score", element.getScore());
                        CWJTable clientWatchlistHit = new CWJTable(new JoinTableID(customer.getId(), element.getId()), element.getScore());
                        // System.out.println(clientWatchlistHit); // I just used this to see if its' working or not, it takes a fair amount of time for all the customers to be checked against elasticsearch...
                        clientWatchlistList.add(clientWatchlistHit);


//                            try {
//                                // myWriter.write(commonFormat.toString());
//                                // myWriter.write(System.getProperty("line.separator"));
//                                // Now I really want to return these!
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                    }
            );
        }
        return clientWatchlistList;
        // myWriter.close();
        // } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
        // }
    }
}
