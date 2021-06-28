package com.vneuron.sbelasticback.Elasticsearch;

import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.GaussDecayFunctionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.RescorerQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        // We can also run language detection here, and depending on the name change the query!

        // I guess the exact value of the boost can be chosen depending on either statistics on the most common types
        // of errors, or on tests...
        // I have used unique tokens, so that only variations due to spelling differences in input data will make a
        // difference (and not the same name repeated)
        shouldQueryList.add("        { \"match\": {\"whole_names.clean\": {\"query\": \"" + wholename + "\", " +
                "\"fuzziness\": \"AUTO\""
                + ", \"boost\": 7"  // Boost the match if it is written almost the  same as in the documents,
//                    now, how to mput this in other queries?
                +"}} }");
        shouldQueryList.add("\n        { \"match\": {\"whole_names.clean_with_shingles\": {\"query\": \"" + wholename +
                "\", " +
                "\"fuzziness\": \"AUTO\""
                + ", \"boost\": 4"  // Boost the match if it is written almost the  same as in the documents,
//                    now, how to input this in other queries?
                +"}} }");
        shouldQueryList.add("\n        { \"multi_match\": {\n" +
                "      \"query\": \"" + wholename + "\",\n" +
                "      \"fields\": [\"whole_names.cross_lingual_double_metaphone^17\", \"whole_names.cross_lingual_double_metaphone_with_shingles^7\", \"whole_names.cross_lingual_beier_morse^7\", \"whole_names.cross_lingual_beier_morse_with_shingles^3\", \"whole_names.phonetic_dm^7\", \"whole_names.phonetic_dm_with_shingles^4\", \"whole_names.phonetic_bm^4\", \"whole_names.phonetic_bm_with_shingles^2\"]\n" +
                "        } } ");

        shouldQueryList.add("\n        { \"match\": { \"whole_names.truncated\": \""+ wholename +"\" }}");

        String rescorerPortion =
                "        { \"match\": {\"whole_names.clean\": {\"query\": \"" + wholename + "\", \"fuzziness\": \"AUTO\"}} }\n";
        String shouldQuery = shouldQueryList.stream().collect(Collectors.joining(","));

        String theQueryAsAString = "{\"bool\" : {\"should\" : [\n" +
                shouldQuery +
                "      ]\n" +
                "    }\n" +
                "}";

        System.out.println(theQueryAsAString);
        StringQuery query = new StringQuery(theQueryAsAString);
        query.addRescorerQuery(new RescorerQuery(
                new StringQuery(rescorerPortion)
        ));

//        StringQuery query = new StringQuery(theQueryAsAString);
//        query.addRescorerQuery(new RescorerQuery(new NativeSearchQueryBuilder().withQuery(QueryBuilders
//                .functionScoreQuery(new FunctionScoreQueryBuilder.FilterFunctionBuilder[] {
//                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(new GaussDecayFunctionBuilder("rate", 0, 10, null, 0.5).setWeight(1f)),
//                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(
//                                new GaussDecayFunctionBuilder("rate", 0, 10, null, 0.5).setWeight(100f)) })
//                .scoreMode(FunctionScoreQuery.ScoreMode.SUM).maxBoost(80f).boostMode(CombineFunction.REPLACE)).build())
//                .withScoreMode(RescorerQuery.ScoreMode.Max).withWindowSize(10));

//        .withRescorerQuery(
//                new RescorerQuery(new NativeSearchQueryBuilder().withQuery(QueryBuilders
//                        .functionScoreQuery(new FunctionScoreQueryBuilder.FilterFunctionBuilder[] {
//                                new FilterFunctionBuilder(new GaussDecayFunctionBuilder("rate", 0, 10, null, 0.5).setWeight(1f)),
//                                new FilterFunctionBuilder(
//                                        new GaussDecayFunctionBuilder("rate", 0, 10, null, 0.5).setWeight(100f)) })
//                        .scoreMode(FunctionScoreQuery.ScoreMode.SUM).maxBoost(80f).boostMode(CombineFunction.REPLACE)).build())
//                        .withScoreMode(ScoreMode.Max).withWindowSize(100))


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
        // Could've also used a multimatch, but I don't know how to deal with the fuzziness and boost...
        for (String wholename : namesList) {
            // I can put all of this within a function right?
            if (!(wholename.isEmpty())) {
                shouldQueryList.add("        { \"match\": {\"whole_names.clean\": {\"query\": \"" + wholename + "\", " +
                        "\"fuzziness\": \"AUTO\""
                        + ", \"boost\": 7"  // Boost the match if it is written almost the  same as in the documents,
//                    now, how to mput this in other queries?
                        + "}} }");
                shouldQueryList.add("\n        { \"match\": {\"whole_names.clean_with_shingles\": {\"query\": \"" + wholename +
                        "\", " +
                        "\"fuzziness\": \"AUTO\""
                        + ", \"boost\": 4"  // Boost the match if it is written almost the  same as in the documents,
//                    now, how to input this in other queries?
                        + "}} }");
                shouldQueryList.add("\n        { \"multi_match\": {\n" +
                        "      \"query\": \"" + wholename + "\",\n" +
                        "      \"fields\": [\"whole_names.cross_lingual_double_metaphone^17\", \"whole_names.cross_lingual_double_metaphone_with_shingles^7\", \"whole_names.cross_lingual_beier_morse^7\", \"whole_names.cross_lingual_beier_morse_with_shingles^3\", \"whole_names.phonetic_dm^7\", \"whole_names.phonetic_dm_with_shingles^4\", \"whole_names.phonetic_bm^4\", \"whole_names.phonetic_bm_with_shingles^2\"]\n" +
                        "        } } ");

                shouldQueryList.add("\n        { \"match\": { \"whole_names.truncated\": \"" + wholename + "\" }}");
            }
        }
        String shouldQuery = shouldQueryList.stream().collect(Collectors.joining(","));



        String theQueryAsAString = "{\"bool\" : {\"should\" : [\n" +
                shouldQuery +
                "      ]\n" +
                "    }\n" +
                "}";
        System.out.println(theQueryAsAString);

        StringQuery query = new StringQuery(theQueryAsAString);
//        query.addRescorerQuery(new RescorerQuery(
//                new StringQuery(rescorerPortion)
//        ));

//        query.addRescorerQuery(new RescorerQuery(new NativeSearchQueryBuilder().withQuery(QueryBuilders
//                .functionScoreQuery(new FunctionScoreQueryBuilder.FilterFunctionBuilder[] {
//                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(new GaussDecayFunctionBuilder("rate", 0, 10, null, 0.5).setWeight(1f)),
//                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(
//                                new GaussDecayFunctionBuilder("rate", 0, 10, null, 0.5).setWeight(100f)) })
//                .scoreMode(FunctionScoreQuery.ScoreMode.SUM).maxBoost(80f).boostMode(CombineFunction.REPLACE)).build())
//                .withScoreMode(RescorerQuery.ScoreMode.Max).withWindowSize(10));

        System.out.println(query.getSource());
        SearchHits<Watchlist> person = operations.search(query, Watchlist.class);
        return person;
    }
}