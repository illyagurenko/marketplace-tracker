package com.gur.marketplace_tracker.strategy;

import com.gur.marketplace_tracker.client.WbApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// класс реализованный паттерном стратегия реализующий его методы
@Component
public class WildberriesStrategy implements MarketplaceStrategy{

    private final WbApiClient wbApiClient;

    @Autowired
    public WildberriesStrategy(WbApiClient wbApiClient) {
        this.wbApiClient = wbApiClient;
    }

    @Override
    public boolean isValidToUse(String url) {
        return url.contains("wildberries.ru");
    }

    @Override
    public String getArticleFromUrl(String url) {
        Pattern pattern = Pattern.compile("(?<=catalog/)\\d+");
        Matcher matcher = pattern.matcher(url);
        String arc;
        if(matcher.find()){
            arc = matcher.group();
            return arc;
        }
        return "not found";
    }

    @Override
    public long getCurrentPrice(String article) {
        return wbApiClient.getCurPrice(article);
    }
}
