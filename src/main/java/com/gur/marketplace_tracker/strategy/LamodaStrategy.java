package com.gur.marketplace_tracker.strategy;

import com.gur.marketplace_tracker.client.LamodaApiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// класс реализованный паттерном стратегия реализующий его методы
@Component
public class LamodaStrategy implements MarketplaceStrategy{

    private final LamodaApiClient lamodaApiClient;

    @Autowired
    public LamodaStrategy(LamodaApiClient lamodaApiClient) {
        this.lamodaApiClient = lamodaApiClient;
    }

    @Override
    public boolean isValidToUse(String url) {
        return url.contains("lamoda.ru");
    }

    @Override
    public String getArticleFromUrl(String url) {
        Pattern pattern = Pattern.compile("(?<=p/)[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(url);
        String arc;
        if(matcher.find()){
            arc = matcher.group();
            return arc;
        }
        return "not found";
    }

    @Override
    public Long getCurrentPrice(String url) {
        return lamodaApiClient.getProductPrice(url);
    }
}
