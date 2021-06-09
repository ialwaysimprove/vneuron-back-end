package com.vneuron.sbelasticback.Elasticsearch;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "watchlist-index")
@Mapping(mappingPath = "watchlist-mapping.json")
@Setting(settingPath = "watchlist-setting.json")
@Data
@Builder

// Just need these two plugins to be installed into elasticsearch for the analyzers to work correctly:
// sudo bin/elasticsearch-plugin install analysis-icu
// sudo bin/elasticsearch-plugin install analysis-phonetic

public class Watchlist implements Serializable {
	@Id
	private String entity_id; // As sir said, I need to better this a bit, separate the fields from one another and so on...
	private String source_document;
	private String id_in_document;
	private String entity_type;
	private String primary_name;
	private HashSet<String> whole_names;

	public Watchlist() {}
// It must have a constructor that encompasses all the fields
	public Watchlist(String entity_id, String source_document, String id_in_document, String entity_type, String primary_name, HashSet<String> whole_names) {
		this.entity_id = entity_id;
		this.source_document = source_document;
		this.id_in_document = id_in_document;
		this.entity_type = entity_type;
		this.primary_name = primary_name;
		this.whole_names = whole_names;
	}
}
