package com.gur.marketplace_tracker.service;

import com.gur.marketplace_tracker.dto.ProductRequest;
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

    public Product createTrack(ProductRequest request){
        String url = request.getUrl();
        Long targetPrice = request.getTargetPrice();
        MarketplaceStrategy strategy = strategies.stream()
                .filter(s -> s.isValidToUse(url))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("сервис не поддерживается"));

        String article = strategy.getArticleFromUrl(url);
        Long curPrice = strategy.getCurrentPrice(url);
        if (curPrice == null) {
            throw new IllegalArgumentException("не удалось получить цену товара");
        }

        Product product = new Product();
        product.setArticle(article);
        product.setCurrentPrice(curPrice);
        product.setTargetPrice(targetPrice);
        product.setIsActive(true);

        return productRepository.save(product);
    }
}
