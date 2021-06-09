package com.vneuron.sbelasticback.Elasticsearch;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.assertj.core.api.Assumptions;

public class ElasticsearchAvailable {

	private final String url;

	private ElasticsearchAvailable(String url) {
		this.url = url;
	}

	public static ElasticsearchAvailable onLocalhost() {
		return new ElasticsearchAvailable("http://localhost:9200");
	}

	private void checkServerRunning() throws Exception {

		try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
			CloseableHttpResponse response = client.execute(new HttpHead(url));
			if (response != null && response.getStatusLine() != null) {
				Assumptions.assumeThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
			}
		} catch (IOException e) {
			throw new Exception(String.format("Elasticsearch Server seems to be down. %s", e.getMessage()));
		}
	}
}
