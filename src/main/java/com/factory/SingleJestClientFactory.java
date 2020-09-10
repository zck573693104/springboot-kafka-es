package com.factory;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


/**
 * 初始化es
 */
@Configuration
public class SingleJestClientFactory implements Serializable {

    private static final long serialVersionUID = -4784476022064315031L;

    private static Logger logger = LoggerFactory.getLogger(SingleJestClientFactory.class);


    @Bean("jestClient")
    public JestClient getClient(){
        return primaryDataSource().getObject();
    }

    @Bean(name = "primaryDataSource")
    public  JestClientFactory primaryDataSource() {
        JestClientFactory instance = null;
                    InputStream inputStream = SingleJestClientFactory.class.getClassLoader().getResourceAsStream("application.properties");
                    Properties properties = new Properties();
                    List<String> serverList = new ArrayList<>();
                    try {
                        properties.load(inputStream);
                        String serverUri = properties.getProperty("es_serverUris");
                        if (serverUri.contains(",")) {
                            serverList.addAll(Arrays.asList(serverUri.split(",")));
                        } else {
                            serverList.add(serverUri);
                        }
                        instance = new JestClientFactory();
                        instance.setHttpClientConfig(new HttpClientConfig
                                .Builder(serverList)
                                .multiThreaded(true)
                                .maxTotalConnection(100)
                                .defaultMaxTotalConnectionPerRoute(2)
                                .readTimeout(10000)
                                .build());
                    } catch (IOException e) {
                        logger.info("加载es地址失败:" + e.getMessage());



        }
        return instance;
    }


}
