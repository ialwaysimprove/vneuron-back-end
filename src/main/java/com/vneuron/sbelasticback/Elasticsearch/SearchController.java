package com.vneuron.sbelasticback.Elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchController {


//    @Autowired
//    WatchlistRepository repository; // We automatically create the repository/index


    public static ElasticsearchAvailable elasticsearchAvailable = ElasticsearchAvailable.onLocalhost();

    ElasticsearchOperations operations;

    @Autowired
    public SearchController(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    @CrossOrigin
    @GetMapping("/person-search/")
    public SearchHits<Watchlist> findByName(@RequestParam String wholename) {
        List<String> shouldQueryList = new ArrayList<>();
        shouldQueryList.add("        { \"match\": { \"whole_names.cross_lingual\": \"" + wholename + "\" }}\n");
        shouldQueryList.add("        { \"match\": { \"whole_names.truncated\": \""+ wholename+"\" }}\n");
        shouldQueryList.add("        { \"match\": { \"whole_names.phonetic\": \""+ wholename+"\" }}\n");
        shouldQueryList.add("        { \"match\": {\"whole_names.text\": {\"query\": \"" + wholename + "\", \"fuzziness\": \"AUTO\"}} }\n");

        String rescorerPortion =
                "        { \"match\": {\"whole_names.text\": {\"query\": \"" + wholename + "\", \"fuzziness\": \"AUTO\"}} }\n";
        String shouldQuery = shouldQueryList.stream().collect(Collectors.joining(","));

        String theQueryAsAString = "{\"bool\" : {\"should\" : [\n" +
                shouldQuery +
                "      ]\n" +
                "    }\n" +
                "}";

        StringQuery query = new StringQuery(theQueryAsAString);
        query.addRescorerQuery(new RescorerQuery(
                new StringQuery(rescorerPortion)
        ));


        System.out.println(query.getSource());
        SearchHits<Watchlist> person = operations.search(query, Watchlist.class);
        return person;
    }

    // This is how rni-index holds the field type name:
    // { "primary_name" : { "type" : "rni_name" }
    // Just add a rescorer, that's all that we need!  This is how it is done in rni-index
    // "rescore": {
    //    "window_size": 3,
    //    "rni_query": {
    //      "rescore_query": {
    //        "rni_function_score": {
    //          "name_score": {
    //            "field": "full_name",
    //            "query_name": "A Ely Taylor",
    //            "score_to_rescore_restriction": 1,
    //            "window_size_allowance": 0.5,
    //            "universe": "parameterUniverseOne"
    //          }
    //        }
    //      },
    //      "query_weight": 0,
    //      "rescore_query_weight": 1
    //    }
    //  }

    public SearchHits<Watchlist> checkName(List<String> namesList) {
        List<String> shouldQueryList = new ArrayList<>();
        String rescorerPortion = "";
        for (String name : namesList) {
            shouldQueryList.add("        { \"match\": { \"whole_names.cross_lingual\": \"" + name + "\" }}\n");
            shouldQueryList.add("        { \"match\": { \"whole_names.truncated\": \""+ name+"\" }}\n");
            shouldQueryList.add("        { \"match\": { \"whole_names.phonetic\": \""+ name+"\" }}\n");
            shouldQueryList.add("        { \"match\": {\"whole_names.text\": {\"query\": \"" + name + "\", \"fuzziness\": \"AUTO\"}} }\n");

            rescorerPortion = "        { \"match\": {\"whole_names.text\": {\"query\": \"" + name + "\", \"fuzziness\": \"AUTO\"}} }\n";
        }
        String shouldQuery = shouldQueryList.stream().collect(Collectors.joining(","));



        String theQueryAsAString = "{\"bool\" : {\"should\" : [\n" +
                shouldQuery +
                "      ]\n" +
                "    }\n" +
                "}";

        StringQuery query = new StringQuery(theQueryAsAString);
        query.addRescorerQuery(new RescorerQuery(
                new StringQuery(rescorerPortion)
        ));

        System.out.println(query.getSource());
        SearchHits<Watchlist> person = operations.search(query, Watchlist.class);
        return person;
    }
}