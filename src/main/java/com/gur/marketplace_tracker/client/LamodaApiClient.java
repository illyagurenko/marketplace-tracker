package com.gur.marketplace_tracker.client;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LamodaApiClient {

    private final RestClient restClient;

    public LamodaApiClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://www.lamoda.ru")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .defaultHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                .defaultHeader("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .build();
    }

    public Long getProductPrice(String productPath) {
        try {
            String html = restClient.get()
                    .uri(productPath)
                    .retrieve()
                    .body(String.class);

            if (html == null || html.isEmpty()) {
                throw new IllegalStateException("Пустой ответ от Lamoda");
            }

            Document doc = Jsoup.parse(html);

            // === Способ 1: Исходный Open Graph (самый быстрый) ===
            Element priceMeta = doc.selectFirst("meta[property=product:price:amount]");
            if (priceMeta != null) {
                return (long) Double.parseDouble(priceMeta.attr("content"));
            }

            // === Способ 2: Поиск в JSON-скриптах (Обычно там хранится состояние React) ===
            // Ищем теги <script>, содержащие информацию о цене
            for (Element script : doc.select("script")) {
                String scriptContent = script.html();
                if (scriptContent.contains("\"price\"")) {
                    // Используем регулярное выражение для быстрого поиска цены
                    Pattern pattern = Pattern.compile("\"price\"\\s*:\\s*(\\d+)");
                    Matcher matcher = pattern.matcher(scriptContent);
                    if (matcher.find()) {
                        return Long.parseLong(matcher.group(1));
                    }
                }
            }

            // === Способ 3: Поиск по визуальным классам (Fallback на случай изменений) ===
            // Актуальные селекторы текущей цены на Lamoda
            Element priceElement = doc.selectFirst(".ui-catalog-search-brand__price-current, span[class*='price']");
            if (priceElement != null) {
                // Оставляем в строке только цифры
                String cleanPrice = priceElement.text().replaceAll("[^0-9]", "");
                if (!cleanPrice.isEmpty()) {
                    return Long.parseLong(cleanPrice);
                }
            }

            // Если ничего не помогло, вероятно, мы поймали капчу
            if (html.contains("cloudflare") || html.contains("captcha")) {
                throw new IllegalStateException("Запрос заблокирован защитой Cloudflare / Капчей");
            }

            throw new IllegalStateException("Тег с ценой не найден на странице товара");

        } catch (NumberFormatException e) {
            throw new RuntimeException("Не удалось распарсить цену как число: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении цены с Lamoda: " + e.getMessage(), e);
        }
    }
}