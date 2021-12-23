package com.github.brutaleo.ifood.pedido.service;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.IOException;

@ApplicationScoped
public class ElasticSearchService {

    private RestHighLevelClient client;

    void startup(@Observes StartupEvent startupEvent) {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(
                                "host.docker.internal",
                                9200,
                                "http")
                )
        );
    }

    void shutdown(@Observes ShutdownEvent shutdownEvent) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void index(String index, String json) {
        IndexRequest ir = new IndexRequest(index).source(json, XContentType.JSON);
        try {
            client.index(ir, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
