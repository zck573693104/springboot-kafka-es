package com.controller;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchScroll;
import io.searchbox.params.Parameters;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ESController {

    @Autowired
    private JestClient jestClient;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping(value = "/es/{index}/{topic}")
    public void readToSend(@PathVariable(name = "index")String index, @PathVariable(name = "topic")String topic) throws Exception {
        SearchResult search = getSearch(index,10, 0);
        String scroll_id = search.getJsonObject().get("_scroll_id").getAsString();
        JestResult jestResult = readMoreFromSearch(scroll_id, 1000L);
        scroll_id = jestResult.getJsonObject().get("_scroll_id").getAsString();
        while (jestResult.isSucceeded() && jestResult.getSourceAsStringList().size() > 0) {
            List<String> sourceAsStringList = jestResult.getSourceAsStringList();
            for (String message:sourceAsStringList){
                kafkaTemplate.send(topic,message);
            }
            jestResult = readMoreFromSearch(scroll_id, 1000L);
            scroll_id = jestResult.getJsonObject().get("_scroll_id").getAsString();
        }
    }

    @GetMapping(value = "/es/{index}/{key}/{input}")
    public void search(@PathVariable(name = "index")String index,@PathVariable(name = "input")String input,@PathVariable(name = "key")String key) throws Exception {
        QueryBuilder builder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery(key, input));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(builder);
        Search search = new Search.Builder(sourceBuilder.toString())
                .addIndex(index)
                .setParameter(Parameters.SIZE, 1000)
                .build();


        JestResult jestResult = jestClient.execute(search);
        List<String> sourceAsStringList = jestResult.getSourceAsStringList();
        for (String message:sourceAsStringList){
            System.out.println(message);
        }
    }

    private SearchResult getSearch(String index, int size, int from) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(size).from(from);
        QueryBuilder must = QueryBuilders.queryStringQuery("*:*");
        searchSourceBuilder.query(must);
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(index)

                .setParameter(Parameters.SIZE, size)
                .setParameter(Parameters.SCROLL, "1m")
                .build();
        SearchResult execute = jestClient.execute(search);
        return execute;
    }

    public JestResult readMoreFromSearch(String scrollId, Long size) throws IOException {
        SearchScroll scroll = new SearchScroll.Builder(scrollId, "1m").build();
        JestResult searchResult = jestClient.execute(scroll);
        return searchResult;

    }


}
