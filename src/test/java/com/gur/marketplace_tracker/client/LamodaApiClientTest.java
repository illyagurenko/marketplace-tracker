package com.gur.marketplace_tracker.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest(LamodaApiClient.class)
public class LamodaApiClientTest {

    @Autowired
    private LamodaApiClient lamodaApiClient;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    @DisplayName("успешное получение цены через мета данные")
    void getPriceFromMeta(){
        // имитация ссылки
        String productPath = "/p/mp002xw07clp/";
        // mockRestServiceServer - виртуальный сервер которы поднял спринг, и он ожидает запросы от реального рестКлиент
        // expect - ждет отправки одного запроса
        // requestTo - смотрит чтоб url была как в основном клиенте
        // andRespond - ответ должен вернуть 200ок из-за withSuccess и место интернета он идет в html файл из ресурсов
        mockRestServiceServer.expect(requestTo("https://www.lamoda.ru" + productPath))
                .andRespond(withSuccess(new ClassPathResource("html/success_meta.html"), MediaType.TEXT_HTML));

        // вызов тестируемого метода
        Long price = lamodaApiClient.getProductPrice(productPath);

        // сравнение
        assertThat(price).isEqualTo(4990L);
    }

    @Test
    @DisplayName("успешное получение цены через json")
    void getPriceFromJson() {
        // логика такая же но проверяется другой способ
        // GIVEN
        String productPath = "/p/mp002xw07clp/";
        mockRestServiceServer.expect(requestTo("https://www.lamoda.ru" + productPath))
                .andRespond(withSuccess(new ClassPathResource("html/success_json.html"), MediaType.TEXT_HTML));

        // WHEN
        Long price = lamodaApiClient.getProductPrice(productPath);

        // THEN
        assertThat(price).isEqualTo(5500L);
    }

    @Test
    @DisplayName("успешное получение цены через css")
    void getPriceFromCss(){
        String rawHtml = "<html><body><span class='ui-catalog-search-brand__price-current'>3 200 ₽</span></body></html>";
        String productPath = "/p/mp002xw07clp/";
        mockRestServiceServer.expect(requestTo("https://www.lamoda.ru" + productPath))
                .andRespond(withSuccess(rawHtml, MediaType.TEXT_HTML));

        // WHEN
        Long price = lamodaApiClient.getProductPrice(productPath);

        // THEN
        assertThat(price).isEqualTo(3200L);
    }
}
