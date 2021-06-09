package com.vneuron.sbelasticback.Elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WatchlistRepository extends ElasticsearchRepository<Watchlist, String> {}
