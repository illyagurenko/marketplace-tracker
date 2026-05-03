package com.gur.marketplace_tracker.strategy;

// интерфейс для реализации паттерна стратегия
// разные сервисы используют разные url, поэтому суть методов одна и та же, но реализация разная
public interface MarketplaceStrategy {

    // может ли приложение обрабатывать url от какого-то сервиса
    boolean isValidToUse(String url);

    // достать артикль из url
    String getArticleFromUrl(String url);

    // достать текущую цену товара по артиклю
    Long getCurrentPrice(String article);


}
