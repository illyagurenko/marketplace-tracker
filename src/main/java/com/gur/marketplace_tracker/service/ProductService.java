package com.gur.marketplace_tracker.service;

import com.gur.marketplace_tracker.entity.Product;
import com.gur.marketplace_tracker.repository.ProductRepository;
import com.gur.marketplace_tracker.strategy.MarketplaceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final List<MarketplaceStrategy> strategies;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(List<MarketplaceStrategy> strategies, ProductRepository productRepository) {
        this.strategies = strategies;
        this.productRepository = productRepository;
    }

    public void createTrack(String url, Long targetPrice){
        MarketplaceStrategy strategy = strategies.stream()
                .filter(s -> s.isValidToUse(url))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("сервис не поддерживается"));

        String article = strategy.getArticleFromUrl(url);
        Long curPrice = strategy.getCurrentPrice(article);

        Product product = new Product();
        product.setArticle(article);
        product.setCurrentPrice(curPrice);
        product.setTargetPrice(targetPrice);
        product.setIsActive(true);

        productRepository.save(product);
    }
}
