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
import java.util.stream.Collectors;

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
            String wholeName = customer.getWhole_name();
            if (!(wholeName.isEmpty() || wholeName.trim().isEmpty() || wholeName.trim().equals("null"))) {
                namesToSearch.add(wholeName);
            }
            String businessName = customer.getBusiness_name();
            if (!(businessName.isEmpty() || businessName.trim().isEmpty() || businessName.trim().equals("null"))) {
                namesToSearch.add(businessName);
            }
            String entireName = "";
            String firstName = customer.getFirst_name();
            if (!(customer.getFirst_name().isEmpty() || customer.getFirst_name().trim().isEmpty() || customer.getFirst_name().trim().equals("null"))) {
                entireName += firstName;
            }
            String lastName = customer.getLast_name();
            if (!(lastName.isEmpty() || lastName.trim().isEmpty() || lastName.trim().equals("null"))) {
                entireName += " ";
                entireName += lastName;
            }
            String maidenName = customer.getMaiden_name();
            if (!(maidenName.isEmpty() || maidenName.trim().isEmpty() || maidenName.trim().equals("null"))) {
                entireName += " ";
                entireName += maidenName;
            }
            namesToSearch.add(entireName);

            String theOtherName = "";
            String managerName = customer.getManager_name();
            if (!(managerName.isEmpty() || managerName.trim().isEmpty() || managerName.trim().equals("null"))) {
                theOtherName += managerName;
            }
//            String gazetteRef = customer.getGazette_ref();
//            if (!(gazetteRef.isEmpty() || gazetteRef.trim().isEmpty() || gazetteRef.trim().equals("null"))) {
//                theOtherName += " ";
//                theOtherName += managerName;
//            }
            namesToSearch.add(theOtherName);

            SearchHits<Watchlist> results = controller.checkName(namesToSearch);


            results.forEach(
                    element -> {

                        CWJTable clientWatchlistHit = new CWJTable(new JoinTableID(customer.getId(), element.getId())
                                , element.getScore(), element.getContent().getPrimary_name(),
                                namesToSearch.stream().collect(Collectors.joining(" ")));
                        System.out.println(clientWatchlistHit); // I just used this to see if its' working or not, it takes a fair amount of time for all the customers to be checked against elasticsearch...
                        clientWatchlistList.add(clientWatchlistHit);
                    }
            );
        }
        return clientWatchlistList;

    }
}
