package com.vneuron.sbelasticback;

import StAXParser.src.main.java.DeployFileLoaderCons;
import StAXParser.src.main.java.DeployFileLoaderFull;
import StAXParser.src.main.java.DeployFileLoaderSDN;
import com.vneuron.sbelasticback.Elasticsearch.Watchlist;
import com.vneuron.sbelasticback.Elasticsearch.WatchlistRepository;
//import com.vneuron.sbelasticback.Postgres.Customer;
//import com.vneuron.sbelasticback.Postgres.CustomerRepository;
//import com.vneuron.sbelasticback.CustomerSearch;
import com.vneuron.sbelasticback.Postgres.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

import static OpenCSVParser.customersLoader.customerCSVParser;

@SpringBootApplication
@EnableElasticsearchRepositories("com.vneuron.sbelasticback.Elasticsearch")
@EnableJpaRepositories("com.vneuron.sbelasticback.Postgres")
public class SbElasticBackApplication {

    @Autowired
    public SbElasticBackApplication(ElasticsearchOperations elasticsearchOperations, WatchlistRepository elasticsearchRepository, CustomerRepository postgresRepository, CustomerSearch customerSearch, CWJTableRepository CWJTableRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticsearchRepository = elasticsearchRepository;
        this.postgresRepository = postgresRepository;
        this.customerSearch = customerSearch;
        this.CWJTableRepository = CWJTableRepository;

    }

    public static void main(String[] args) {
        SpringApplication.run(SbElasticBackApplication.class, args);


    }

    // Replace field injections with constructor injections!
    ElasticsearchOperations elasticsearchOperations;
    WatchlistRepository elasticsearchRepository;

    CustomerRepository postgresRepository;

    CustomerSearch customerSearch;

    CWJTableRepository CWJTableRepository;

    @PreDestroy
    public void deleteIndex() {
        elasticsearchOperations.indexOps(Watchlist.class).delete();

        postgresRepository.deleteAll(); // Delete all data from postgres, sad thing is, I don't know how to delete the table itself! :'(
    } // Before stopping the Elasticsearch application, delete the newly created index

    @PostConstruct
    public void insertDataSample() {
        elasticsearchRepository.deleteAll();
        elasticsearchOperations.indexOps(Watchlist.class).refresh();

        System.out.println("Saving SDN watchlist data in progress... ");
        ArrayList<Watchlist> sdn = DeployFileLoaderSDN.convert();
        elasticsearchRepository.saveAll(sdn);

        System.out.println("Saving FULL watchlist data in progress... ");
        ArrayList<Watchlist> full = DeployFileLoaderFull.convert();
        elasticsearchRepository.saveAll(full);

        System.out.println("Saving CONS watchlist data in progress... ");
        ArrayList<Watchlist> cons = DeployFileLoaderCons.convert();
        elasticsearchRepository.saveAll(cons);

        postgresRepository.deleteAll();

        System.out.println("Saving Vneuron Customers data in progress... ");
        ArrayList<Customer> vncustomers = customerCSVParser();
        postgresRepository.saveAll(vncustomers);

        List<Customer> pgsavedcustomers = postgresRepository.findAll();

        List<CWJTable> searchHits = customerSearch.checkCustomers(pgsavedcustomers);

        CWJTableRepository.saveAll(searchHits);

        // System.out.println(pgsavedcustomers); // I'll leave it just in case, because this is the things that we stored into postgresql!



        // convert that naive output to something we can actually use...

        // The next step is probably to have the search results stored into the database instead of in a file
        // How to save them in the database?
        // (customer_id + watchlist_id) = search_hit_id + match/score.. What about this?
        System.out.println("Everything is done right now!... Ready to serve search requests"); // Probably will be better if the files can be loaded and searched on, but searching is allowed at the same time?
    }

}
