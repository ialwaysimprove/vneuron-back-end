package com.vneuron.sbelasticback.Elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "watchlist-index")
@Mapping(mappingPath = "watchlist-mapping.json")
@Setting(settingPath = "watchlist-setting.json")
@AllArgsConstructor
@NoArgsConstructor
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
}
