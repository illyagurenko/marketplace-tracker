package com.gur.marketplace_tracker.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WbApiClient {

    private final RestClient restClient;

    @Autowired
    public WbApiClient(RestClient restClient) {
        this.restClient = RestClient.create("https://wb.ru");
    }

    public Long getCurPrice(String article){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cards/v2/detail")
                        .queryParam("appType", 1)
                        .queryParam("curr", "rub")
                        .queryParam("dest", -1255987)
                        .queryParam("spp", 30)
                        .queryParam("ab_testing", false)
                        .queryParam("nm", article)
                        .build())
                .retrieve()
                .body(Long.class);
    }
}
